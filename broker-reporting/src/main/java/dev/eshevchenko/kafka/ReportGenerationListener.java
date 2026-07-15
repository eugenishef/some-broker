package dev.eshevchenko.kafka;

import dev.eshevchenko.service.ReportGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportGenerationListener {

  private final ReportGenerationService reportGenerationService;

  @KafkaListener(topics = "report-requests", groupId = "broker-api")
  public void onReportRequested(ReportGenerationRequestedEvent event) {
    MDC.put("reportId", event.reportId().toString());
    MDC.put("operation", "reportGenerationTriggered");
    try {
      log.info("Получено событие генерации отчета из Kafka");
      reportGenerationService.generate(event.reportId());
    } finally {
      MDC.remove("reportId");
      MDC.remove("operation");
    }
  }
}