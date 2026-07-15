package dev.eshevchenko.dto.request;

import dev.eshevchenko.enums.Status;
import java.util.UUID;

public record ReportSearchRequest(
  UUID clientId,
  String type,
  Status status,
  int page,
  int size
) {}