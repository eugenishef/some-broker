package dev.eshevchenko.service.impl;

import dev.eshevchenko.dto.ClientData;
import dev.eshevchenko.dto.TransactionData;
import dev.eshevchenko.entity.Report;
import dev.eshevchenko.entity.ReportVersionEntity;
import dev.eshevchenko.enums.Status;
import dev.eshevchenko.exception.ClientNotFoundException;
import dev.eshevchenko.repository.ClientRepository;
import dev.eshevchenko.repository.ReportRepository;
import dev.eshevchenko.repository.ReportVersionRepository;
import dev.eshevchenko.repository.TransactionRepository;
import dev.eshevchenko.service.PdfRenderer;
import dev.eshevchenko.service.ReportGenerationService;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class ReportGenerationServiceImpl implements ReportGenerationService {

  private final ReportRepository reportRepository;
  private final ReportVersionRepository reportVersionRepository;
  private final ClientRepository clientRepository;
  private final TransactionRepository transactionRepository;
  private final PdfRenderer pdfRenderer;

  @Qualifier("reportExecutor")
  private final Executor reportExecutor;

  @Async("reportExecutor")
  @Transactional
  public CompletableFuture<Void> generate(UUID reportId) {
    MDC.put("reportId", reportId.toString());
    MDC.put("operation", "generateReport");
    try {
      Report report = reportRepository.findById(reportId).orElse(null);
      if (report == null) {
        log.warn("Отчет не найден, пропускаем обработку");
        return CompletableFuture.completedFuture(null);
      }

      if (report.getStatus() != Status.PENDING) {
        log.info("Отчет уже в статусе {}, повторная обработка пропущена", report.getStatus());
        return CompletableFuture.completedFuture(null);
      }

      report.setStatus(Status.IN_PROGRESS);
      reportRepository.save(report);
      log.info("Начата генерация отчета");

      CompletableFuture<ClientData> clientDataFuture = CompletableFuture.supplyAsync(
        () -> clientRepository.fetchClientData(report.getClientId())
          .orElseThrow(() -> new ClientNotFoundException(
            "Клиент с id=" + report.getClientId() + " не найден")),
        reportExecutor);

      CompletableFuture<List<TransactionData>> transactionsFuture = CompletableFuture.supplyAsync(
        () -> transactionRepository
          .findByClientIdAndCreatedAtBetween(
            report.getClientId(), report.getPeriodFrom(), report.getPeriodTo())
          .stream()
          .map(tx -> new TransactionData(
            tx.getId(),
            tx.getAmount(),
            tx.getCurrency(),
            tx.getStatus().name(),
            tx.getCreatedAt()))
          .toList(),
        reportExecutor);

      return clientDataFuture
        .thenCombineAsync(transactionsFuture,
          (clientData, transactions) -> pdfRenderer.render(clientData, transactions),
          reportExecutor)
        .thenAcceptAsync(pdfBytes -> {
          ReportVersionEntity version = new ReportVersionEntity();
          version.setReportId(report.getId());
          version.setVersionNumber(1);
          version.setContent(pdfBytes);
          reportVersionRepository.save(version);

          report.setStatus(Status.READY);
          reportRepository.save(report);
          log.info("Отчет успешно сгенерирован");
        }, reportExecutor)
        .exceptionally(ex -> {
          log.error("Ошибка генерации отчета", ex);
          report.setStatus(Status.FAILED);
          reportRepository.save(report);
          return null;
        });
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }
}