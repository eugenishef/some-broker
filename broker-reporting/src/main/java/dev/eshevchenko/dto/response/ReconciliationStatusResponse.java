package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;
import java.time.Instant;

public record ReconciliationStatusResponse(
  String id,
  Status status,
  Instant createdAt
) {}