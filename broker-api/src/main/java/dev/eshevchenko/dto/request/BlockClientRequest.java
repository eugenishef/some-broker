package dev.eshevchenko.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BlockClientRequest(
  @NotBlank String reason
) {}