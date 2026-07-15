package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;

public record StartReconciliationResponse(
  String id,
  Status status
) {}