package dev.eshevchenko.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record DisputeRequest(
  @NotEmpty List<UUID> operationIds,
  @NotBlank String comment
) {}