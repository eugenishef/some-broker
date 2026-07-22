package dev.eshevchenko.dto.event;

import dev.eshevchenko.enums.ReportStatus;
import java.util.UUID;

public record CreateReportResponse(
  UUID id,
  ReportStatus status
) {}