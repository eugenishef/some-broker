package dev.eshevchenko.dto.request;

import dev.eshevchenko.dto.TransactionData;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SupplementReportRequest(
  long expectedVersion,
  @NotEmpty List<TransactionData> additionalData
) {}