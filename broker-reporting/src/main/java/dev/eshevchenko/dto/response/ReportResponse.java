package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;
import java.time.Instant;
import java.util.UUID;

public record ReportResponse(
  String id,
  UUID clientId,
  String type,
  Status status,
  int currentVersion,
  Instant createdAt
) {}