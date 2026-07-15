package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;

public record CreateReportResponse(
  String id,
  Status status
) {}