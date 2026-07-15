package dev.eshevchenko.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReportImportData(
  UUID clientId,
  String clientName,
  String inn,
  String reportType,
  LocalDate periodFrom,
  LocalDate periodTo,
  String currency,
  List<ReportOperationDto> operations
) {}