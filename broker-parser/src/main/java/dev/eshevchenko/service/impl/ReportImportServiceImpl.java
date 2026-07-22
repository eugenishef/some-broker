package dev.eshevchenko.service.impl;

import dev.eshevchenko.dto.event.CreateReportResponse;
import dev.eshevchenko.dto.ReportCreatedEvent;
import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.dto.ReportOperationDto;
import dev.eshevchenko.entity.Report;
import dev.eshevchenko.entity.ReportOperation;
import dev.eshevchenko.enums.ReportStatus;
import dev.eshevchenko.mapper.ReportMapper;
import dev.eshevchenko.parser.xlsx.ReportExcelParser;
import dev.eshevchenko.repository.ReportOperationRepository;
import dev.eshevchenko.repository.ReportRepository;
import dev.eshevchenko.service.ReportImportService;
import dev.eshevchenko.listener.ReportVersionCreator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportImportServiceImpl implements ReportImportService {

  private static final String IMPORTED_TYPE = "IMPORTED";

  private final ReportRepository reportRepository;
  private final ReportOperationRepository operationRepository;
  private final ReportExcelParser parser;
  private final ReportMapper mapper;
  private final ApplicationEventPublisher eventPublisher;
  private final EntityManager entityManager;
  private final ReportVersionCreator reportVersionCreator;

  @Value("${app.report.import.batch-size:1}")
  private int batchSize;

  @Override
  @Transactional
  @CacheEvict(value = "reports", allEntries = true)
  public CreateReportResponse importReport(MultipartFile file) {

    log.info("Import report started {}", file.getOriginalFilename());

    ReportImportData data = parser.parse(file);

    Optional<Report> existingReport = reportRepository.findByClientIdAndTypeAndPeriodFromAndPeriodTo(
      data.clientId(), IMPORTED_TYPE, data.periodFrom(), data.periodTo());

    UUID reportId;

    if (existingReport.isPresent()) {
      reportId = existingReport.get().getId();
      log.info("Найден существующий отчёт reportId={}, создаём новую версию", reportId);

      operationRepository.deleteAllByReportId(reportId);
      operationRepository.flush();
    } else {

      Report report = mapper.toEntity(data);
      Report savedReport = reportRepository.save(report);
      reportId = savedReport.getId();
      reportRepository.flush();

    }

    saveOperationsInBatches(data.operations(), reportId);

    reportVersionCreator.createNextVersion(reportId, ReportStatus.CREATED, data);

    Report reportToUpdate = reportRepository.findById(reportId)
      .orElseThrow(() -> new EntityNotFoundException("Отчёт не найден: " + reportId));
    reportToUpdate.setStatus(ReportStatus.CREATED);
    Report finalReport = reportRepository.save(reportToUpdate);

    eventPublisher.publishEvent(new ReportCreatedEvent(reportId));

    log.info("Import report finished, reportId={}, operations={}",
      reportId, data.operations().size());

    return new CreateReportResponse(reportId, finalReport.getStatus());
  }

  private void saveOperationsInBatches(List<ReportOperationDto> dtos, UUID reportId) {
    List<ReportOperation> operations = dtos.stream()
      .map(mapper::toOperation)
      .peek(operation -> operation.setReportId(reportId))
      .toList();

    for (List<ReportOperation> chunk : ListUtils.partition(operations, batchSize)) {
      operationRepository.saveAll(chunk);
      operationRepository.flush();
      entityManager.clear();
    }
  }
}