package dev.eshevchenko.dto;

import java.util.UUID;

public record ReportCreatedEvent(
  UUID reportId
){}