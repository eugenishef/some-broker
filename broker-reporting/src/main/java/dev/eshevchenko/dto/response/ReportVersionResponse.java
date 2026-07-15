package dev.eshevchenko.dto.response;

import java.time.Instant;

public record ReportVersionResponse(
  int versionNumber,
  Instant createdAt
) {}