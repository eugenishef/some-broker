package dev.eshevchenko.service.impl;

import dev.eshevchenko.dto.request.CreateReportRequest;
import dev.eshevchenko.dto.request.DisputeRequest;
import dev.eshevchenko.dto.request.ReportSearchRequest;
import dev.eshevchenko.dto.request.SupplementReportRequest;
import dev.eshevchenko.dto.response.CreateReportResponse;
import dev.eshevchenko.dto.response.DisputeResponse;
import dev.eshevchenko.dto.response.ReportResponse;
import dev.eshevchenko.dto.response.ReportVersionResponse;
import dev.eshevchenko.entity.Dispute;
import dev.eshevchenko.entity.Report;
import dev.eshevchenko.entity.ReportVersionEntity;
import dev.eshevchenko.enums.Status;
import dev.eshevchenko.exception.ReportNotFoundException;
import dev.eshevchenko.exception.ReportVersionConflictException;
import dev.eshevchenko.exception.ReportVersionNotFoundException;
import dev.eshevchenko.kafka.ReportGenerationRequestedEvent;
import dev.eshevchenko.mapper.ReportMapper;
import dev.eshevchenko.repository.DisputeRepository;
import dev.eshevchenko.repository.ReportRepository;
import dev.eshevchenko.repository.ReportVersionRepository;
import dev.eshevchenko.service.ReportService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportRepository reportRepository;
  private final ReportVersionRepository reportVersionRepository;
  private final DisputeRepository disputeRepository;
  private final ReportMapper reportMapper;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  @Transactional
  public CreateReportResponse createReport(CreateReportRequest request) {
    MDC.put("clientId", request.clientId().toString());
    MDC.put("operation", "createReport");
    try {
      log.info("Создание запроса на отчет, type={}", request.type());

      Report entity = reportMapper.toEntity(request);
      Report saved = reportRepository.save(entity);

      MDC.put("reportId", saved.getId().toString());
      log.info("Запрос на отчет создан, публикуем событие в Kafka");

      kafkaTemplate.send("report-requests", saved.getId().toString(),
        new ReportGenerationRequestedEvent(saved.getId()));

      return new CreateReportResponse(saved.getId().toString(), saved.getStatus());
    } finally {
      MDC.remove("clientId");
      MDC.remove("operation");
      MDC.remove("reportId");
    }
  }

  @Override
  public Page<ReportResponse> searchReports(ReportSearchRequest request) {
    MDC.put("operation", "searchReports");
    try {
      log.info("Поиск отчетов, type={}, status={}", request.type(), request.status());
      Pageable pageable = PageRequest.of(request.page(), request.size());
      return reportRepository
        .search(request.clientId(), request.type(), request.status(), pageable)
        .map(reportMapper::toResponse);
    } finally {
      MDC.remove("operation");
    }
  }

  @Override
  public ReportResponse getReport(UUID id) {
    MDC.put("reportId", id.toString());
    MDC.put("operation", "getReport");
    try {
      log.info("Запрос отчета");
      Report report = findReportOrThrow(id);
      return reportMapper.toResponse(report);
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }

  @Override
  @Transactional
  public ReportResponse getOrCreateForPeriod(UUID clientId, LocalDate from, LocalDate to) {
    MDC.put("clientId", clientId.toString());
    MDC.put("operation", "getOrCreateReportForPeriod");
    try {
      return reportRepository.findByClientIdAndPeriodFromAndPeriodTo(clientId, from, to)
        .map(reportMapper::toResponse)
        .orElseGet(() -> {
          log.info("Отчет за период не найден, создаем новый");
          CreateReportResponse created = createReport(
            new CreateReportRequest(clientId, "PERIOD", from, to));
          return getReport(UUID.fromString(created.id()));
        });
    } finally {
      MDC.remove("clientId");
      MDC.remove("operation");
    }
  }

  @Override
  public List<ReportVersionResponse> getVersions(UUID id) {
    MDC.put("reportId", id.toString());
    MDC.put("operation", "getVersions");
    try {
      log.info("Запрос списка версий отчета");
      if (!reportRepository.existsById(id)) {
        log.warn("Отчет не найден");
        throw new ReportNotFoundException("Отчет с id=" + id + " не найден");
      }
      return reportVersionRepository.findByReportIdOrderByVersionNumberAsc(id).stream()
        .map(reportMapper::toVersionResponse)
        .toList();
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }

  @Override
  public ReportVersionResponse getVersion(UUID id, int versionNumber) {
    MDC.put("reportId", id.toString());
    MDC.put("version", String.valueOf(versionNumber));
    MDC.put("operation", "getVersion");
    try {
      log.info("Запрос конкретной версии отчета");
      return reportVersionRepository.findByReportIdAndVersionNumber(id, versionNumber)
        .map(reportMapper::toVersionResponse)
        .orElseThrow(() -> {
          log.warn("Версия отчета не найдена");
          return new ReportVersionNotFoundException(
            "Версия %d отчета с id=%s не найдена".formatted(versionNumber, id));
        });
    } finally {
      MDC.remove("reportId");
      MDC.remove("version");
      MDC.remove("operation");
    }
  }

  @Override
  public byte[] getPdf(UUID id) {
    MDC.put("reportId", id.toString());
    MDC.put("operation", "getPdf");
    try {
      log.info("Запрос PDF отчета");
      Report report = findReportOrThrow(id);
      return report.getContent();
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }

  @Override
  @Retryable(
    retryFor = ObjectOptimisticLockingFailureException.class,
    maxAttempts = 3,
    backoff = @Backoff(delay = 100, multiplier = 2)
  )
  @Transactional
  public ReportResponse supplementReport(UUID reportId, SupplementReportRequest request) {
    MDC.put("reportId", reportId.toString());
    MDC.put("operation", "supplementReport");
    try {
      Report report = findReportOrThrow(reportId);

      if (report.getVersion() != request.expectedVersion()) {
        log.warn("Конфликт версий: клиент ожидал {}, актуальная {}",
          request.expectedVersion(), report.getVersion());
        throw new ReportVersionConflictException(
          "Отчет был изменен другим процессом, получите актуальную версию и повторите запрос");
      }

      int nextVersionNumber = reportVersionRepository.countByReportId(reportId) + 1;
      ReportVersionEntity versionEntity = new ReportVersionEntity();
      versionEntity.setReportId(reportId);
      versionEntity.setVersionNumber(nextVersionNumber);
      versionEntity.setContent(report.getContent());
      reportVersionRepository.save(versionEntity);

      Report saved = reportRepository.save(report);

      log.info("Отчет дополнен, бизнес-версия={}, optimisticVersion={}",
        nextVersionNumber, saved.getVersion());
      return reportMapper.toResponse(saved);
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }

  @Recover
  public ReportResponse recoverSupplement(
    ObjectOptimisticLockingFailureException ex, UUID reportId, SupplementReportRequest request) {
    log.error("Не удалось дополнить отчет после нескольких попыток, гонка обновлений", ex);
    throw new ReportVersionConflictException(
      "Не удалось обновить отчет из-за конкурентных изменений, повторите попытку позже");
  }

  @Override
  @Transactional
  public DisputeResponse disputeOperations(UUID reportId, DisputeRequest request) {
    MDC.put("reportId", reportId.toString());
    MDC.put("operation", "disputeOperations");
    try {
      if (!reportRepository.existsById(reportId)) {
        log.warn("Отчет не найден для оспаривания");
        throw new ReportNotFoundException("Отчет с id=" + reportId + " не найден");
      }

      Dispute dispute = new Dispute();
      dispute.setReportId(reportId);
      dispute.setOperationIds(request.operationIds());
      dispute.setComment(request.comment());
      dispute.setStatus(Status.PENDING);

      Dispute saved = disputeRepository.save(dispute);
      log.info("Создан спор по {} операциям", request.operationIds().size());

      return new DisputeResponse(saved.getId().toString(), saved.getStatus());
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }

  private Report findReportOrThrow(UUID id) {
    return reportRepository.findById(id)
      .orElseThrow(() -> {
        log.warn("Отчет не найден");
        return new ReportNotFoundException("Отчет с id=" + id + " не найден");
      });
  }
}