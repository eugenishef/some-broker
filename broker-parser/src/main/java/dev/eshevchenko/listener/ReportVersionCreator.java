package dev.eshevchenko.listener;

import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.entity.ReportVersionEntity;
import dev.eshevchenko.enums.ReportStatus;
import dev.eshevchenko.service.ReportVersionService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportVersionCreator {

  private final ReportVersionService versionService;

  @Retryable(
    retryFor = DataIntegrityViolationException.class,
    maxAttempts = 3,
    backoff = @Backoff(delay = 100)
  )
  public ReportVersionEntity createNextVersion(UUID reportId, ReportStatus status, ReportImportData data) {
    return versionService.saveNewVersion(reportId, status, data);
  }
}