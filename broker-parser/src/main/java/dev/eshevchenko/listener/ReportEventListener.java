package dev.eshevchenko.listener;

import dev.eshevchenko.dto.ReportCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReportEventListener {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Async("reportEventsExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onReportCreated(ReportCreatedEvent event) {
    kafkaTemplate.send("report-created", event.reportId().toString(), event);
  }
}