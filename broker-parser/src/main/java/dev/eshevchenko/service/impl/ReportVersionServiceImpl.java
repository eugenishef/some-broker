package dev.eshevchenko.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.entity.ReportVersionEntity;
import dev.eshevchenko.enums.ReportStatus;
import dev.eshevchenko.exception.ReportImportException;
import dev.eshevchenko.repository.ReportVersionRepository;
import dev.eshevchenko.service.ReportVersionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportVersionServiceImpl implements ReportVersionService {
  private final ReportVersionRepository reportVersionRepository;
  private final ObjectMapper objectMapper;

  @Transactional
  public ReportVersionEntity saveNewVersion(
    UUID reportId, ReportStatus status, ReportImportData data) {
    reportVersionRepository.clearActualFlag(reportId);

    ReportVersionEntity version = new ReportVersionEntity();
    version.setReportId(reportId);
    version.setVersionNumber(nextVersionNumber(reportId));
    version.setStatus(status);
    version.setActual(true);
    version.setContent(serializeSnapshot(data));

    return reportVersionRepository.save(version);
  }

  private int nextVersionNumber(UUID reportId) {
    return reportVersionRepository.findMaxVersionNumberByReportId(reportId)
      .map(max -> max + 1)
      .orElse(1);
  }

  private byte[] serializeSnapshot(ReportImportData data) {
    try {
      return objectMapper.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      throw new ReportImportException("Не удалось сериализовать снимок отчёта", e);
    }
  }
}