package dev.eshevchenko.dto.request;

import dev.eshevchenko.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
  @NotBlank String clientId,
  @NotBlank String currency,
  @NotNull AccountType type
) {}