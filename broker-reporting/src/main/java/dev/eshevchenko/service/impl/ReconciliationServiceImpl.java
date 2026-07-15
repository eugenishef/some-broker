package dev.eshevchenko.service.impl;

import dev.eshevchenko.dto.DiscrepancyDto;
import dev.eshevchenko.dto.request.StartReconciliationRequest;
import dev.eshevchenko.dto.response.ReconciliationResultResponse;
import dev.eshevchenko.dto.response.ReconciliationStatusResponse;
import dev.eshevchenko.dto.response.StartReconciliationResponse;
import dev.eshevchenko.dto.view.DiscrepancyView;
import dev.eshevchenko.entity.Reconciliation;
import dev.eshevchenko.entity.ReconciliationDiscrepancy;
import dev.eshevchenko.enums.Status;
import dev.eshevchenko.exception.ReconciliationNotFoundException;
import dev.eshevchenko.kafka.ReconciliationRequestedEvent;
import dev.eshevchenko.mapper.ReconciliationMapper;
import dev.eshevchenko.repository.ReconciliationDiscrepancyRepository;
import dev.eshevchenko.repository.ReconciliationRepository;
import dev.eshevchenko.repository.TransactionRepository;
import dev.eshevchenko.service.ReconciliationService;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReconciliationServiceImpl implements ReconciliationService {

  private final ReconciliationRepository reconciliationRepository;
  private final ReconciliationDiscrepancyRepository reconciliationDiscrepancyRepository;
  private final ReconciliationMapper reconciliationMapper;
  private final TransactionRepository transactionRepository;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  @Transactional
  public StartReconciliationResponse startReconciliation(StartReconciliationRequest request) {
    MDC.put("clientId", request.clientId().toString());
    MDC.put("operation", "startReconciliation");
    try {
      log.info("Запуск сверки, period={}..{}", request.periodFrom(), request.periodTo());

      Reconciliation entity = reconciliationMapper.toEntity(request);
      Reconciliation saved = reconciliationRepository.save(entity);

      MDC.put("reconciliationId", saved.getId().toString());
      log.info("Сверка создана, публикуем событие в Kafka");

      kafkaTemplate.send("reconciliation-requests", saved.getId().toString(),
        new ReconciliationRequestedEvent(saved.getId()));

      return new StartReconciliationResponse(saved.getId().toString(), saved.getStatus());
    } finally {
      MDC.remove("clientId");
      MDC.remove("operation");
      MDC.remove("reconciliationId");
    }
  }

  @Override
  public ReconciliationStatusResponse getStatus(UUID id) {
    MDC.put("reconciliationId", id.toString());
    MDC.put("operation", "getReconciliationStatus");
    try {
      log.info("Запрос статуса сверки");
      Reconciliation reconciliation = reconciliationRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("Сверка не найдена");
          return new ReconciliationNotFoundException("Сверка с id=" + id + " не найдена");
        });
      return reconciliationMapper.toStatusResponse(reconciliation);
    } finally {
      MDC.remove("reconciliationId");
      MDC.remove("operation");
    }
  }

  @Override
  public ReconciliationResultResponse getResult(UUID id) {
    MDC.put("reconciliationId", id.toString());
    MDC.put("operation", "getReconciliationResult");
    try {
      log.info("Запрос результата сверки");
      if (!reconciliationRepository.existsById(id)) {
        log.warn("Сверка не найдена");
        throw new ReconciliationNotFoundException("Сверка с id=" + id + " не найдена");
      }
      List<DiscrepancyView> discrepancies = reconciliationRepository.findDiscrepancies(id);
      log.info("Найдено расхождений: {}", discrepancies.size());
      return new ReconciliationResultResponse(id.toString(), discrepancies);
    } finally {
      MDC.remove("reconciliationId");
      MDC.remove("operation");
    }
  }

  @Async("reconciliationExecutor")
  @Transactional
  public CompletableFuture<Void> process(UUID reconciliationId) {
    MDC.put("reconciliationId", reconciliationId.toString());
    MDC.put("operation", "processReconciliation");
    try {
      Reconciliation reconciliation = reconciliationRepository.findById(reconciliationId).orElse(null);
      if (reconciliation == null) {
        log.warn("Сверка не найдена, пропускаем обработку");
        return CompletableFuture.completedFuture(null);
      }
      if (reconciliation.getStatus() != Status.PENDING) {
        log.info("Сверка уже в статусе {}, повторная обработка пропущена", reconciliation.getStatus());
        return CompletableFuture.completedFuture(null);
      }

      reconciliation.setStatus(Status.RUNNING);
      reconciliationRepository.save(reconciliation);
      log.info("Сверка переведена в статус RUNNING");

      List<CompletableFuture<List<DiscrepancyDto>>> checks = List.of();

//      List<CompletableFuture<List<DiscrepancyDto>>> checks = List.of(
//        CompletableFuture.supplyAsync(() -> compareBalances(reconciliation)),
//        CompletableFuture.supplyAsync(() -> compareTransactionCounts(reconciliation)),
//        CompletableFuture.supplyAsync(() -> compareStatuses(reconciliation))
//      );

      return CompletableFuture.allOf(checks.toArray(new CompletableFuture[0]))
        .thenApply(v -> checks.stream()
          .flatMap(f -> f.join().stream())
          .toList())
        .thenAccept(discrepancies -> saveResult(reconciliation, discrepancies))
        .exceptionally(ex -> {
          log.error("Ошибка выполнения сверки", ex);
          reconciliation.setStatus(Status.FAILED);
          reconciliationRepository.save(reconciliation);
          return null;
        });
    } finally {
      MDC.remove("reconciliationId");
      MDC.remove("operation");
    }
  }

  private void saveResult(Reconciliation reconciliation, List<DiscrepancyDto> discrepancies) {
    List<ReconciliationDiscrepancy> entities = discrepancies.stream()
      .map(dto -> toDiscrepancyEntity(reconciliation.getId(), dto))
      .toList();
    reconciliationDiscrepancyRepository.saveAll(entities);

    reconciliation.setStatus(Status.COMPLETED);
    reconciliationRepository.save(reconciliation);
    log.info("Сверка завершена, расхождений: {}", discrepancies.size());
  }

  private ReconciliationDiscrepancy toDiscrepancyEntity(UUID reconciliationId, DiscrepancyDto dto) {
    ReconciliationDiscrepancy entity = new ReconciliationDiscrepancy();
    entity.setReconciliationId(reconciliationId);
    entity.setField(dto.field());
    entity.setExpected(dto.expected());
    entity.setActual(dto.actual());
    return entity;
  }
    // TODO: сравнить баланс из внутренней системы с внешним источником за период
    // TODO: сравнить количество транзакций за период
    // TODO: сравнить статусы операций между источниками
}