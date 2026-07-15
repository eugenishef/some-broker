package dev.eshevchenko.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record StartReconciliationRequest(
  @NotNull UUID clientId,
  @NotNull LocalDate periodFrom,
  @NotNull LocalDate periodTo
) {}