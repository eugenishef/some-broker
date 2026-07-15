package dev.eshevchenko.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReportOperationDto(
  UUID operationId,
  LocalDate operationDate,
  BigDecimal amount,
  String currency,
  String operationType,
  String status
) {
}