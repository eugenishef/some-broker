package dev.eshevchenko.dto;

import dev.eshevchenko.enums.ReportStatus;

public record CreateReportResponse(
  String id,
  ReportStatus status
) {}