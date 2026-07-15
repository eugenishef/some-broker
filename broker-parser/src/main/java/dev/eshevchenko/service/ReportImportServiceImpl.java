package dev.eshevchenko.service;

import dev.eshevchenko.dto.CreateReportResponse;
import dev.eshevchenko.dto.ReportCreatedEvent;
import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.dto.ReportOperationDto;
import dev.eshevchenko.entity.Report;
import dev.eshevchenko.entity.ReportOperation;
import dev.eshevchenko.entity.ReportVersionEntity;
import dev.eshevchenko.enums.ReportStatus;
import dev.eshevchenko.mapper.ReportMapper;
import dev.eshevchenko.parser.xlsx.ReportExcelParser;
import dev.eshevchenko.repository.ReportOperationRepository;
import dev.eshevchenko.repository.ReportRepository;
import dev.eshevchenko.repository.ReportVersionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportImportServiceImpl implements ReportImportService {

  private final ReportRepository reportRepository;
  private final ReportVersionRepository reportVersionRepository;
  private final ReportOperationRepository operationRepository;
  private final ReportExcelParser parser;
  private final ReportMapper mapper;
  private final KafkaTemplate<String,Object> kafkaTemplate;

  private static final int BATCH_SIZE = 1;

  @Override
  @Transactional
  @CacheEvict(value = "reports", allEntries = true)
  public CreateReportResponse importReport(MultipartFile file) {
    UUID reportId = UUID.randomUUID();

    log.info("Import report started {}", file.getOriginalFilename());

    ReportImportData data = parser.parse(file);

    Report report = new Report();
    report.setId(reportId);
    report.setClientId(data.clientId());
    report.setClientName(data.clientName());
    report.setInn(data.inn());
    report.setType("IMPORTED");
    report.setStatus(ReportStatus.PROCESSING);
    report.setPeriodFrom(data.periodFrom());
    report.setPeriodTo(data.periodTo());
    report.setCurrency(data.currency());

    reportRepository.save(report);

    List<ReportOperation> batch = new ArrayList<>();

    for (ReportOperationDto dto : data.operations()) {
      ReportOperation operation = mapper.toOperation(dto);
      operation.setReportId(reportId);
      batch.add(operation);

      if (batch.size() == BATCH_SIZE) {
        operationRepository.saveAll(batch);
        operationRepository.flush();
        batch.clear();
      }
    }

    if (!batch.isEmpty()) {
      operationRepository.saveAll(batch);
    }

    ReportVersionEntity version = new ReportVersionEntity();
    version.setReportId(reportId);
    version.setVersionNumber(1);
    version.setStatus(ReportStatus.CREATED);
    reportVersionRepository.save(version);

    report.setStatus(ReportStatus.CREATED);

    kafkaTemplate.send(
      "report-created",
      reportId.toString(),
      new ReportCreatedEvent(reportId)
    );

    return new CreateReportResponse(reportId.toString(), report.getStatus());
  }
}