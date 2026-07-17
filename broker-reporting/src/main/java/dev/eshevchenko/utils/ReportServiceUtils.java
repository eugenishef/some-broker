package dev.eshevchenko.utils;

import dev.eshevchenko.entity.Report;
import dev.eshevchenko.exception.ReportNotFoundException;
import dev.eshevchenko.repository.ReportRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportServiceUtils {

  private final ReportRepository reportRepository;

  public Report findReportOrThrow(UUID id) {
    return reportRepository.findById(id)
      .orElseThrow(() -> {
        log.warn("Отчет не найден");
        return new ReportNotFoundException("Отчет с id=" + id + " не найден");
      });
  }
}