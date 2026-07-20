package dev.eshevchenko.dto.request;

import dev.eshevchenko.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateAccountRequest(
  @NotBlank UUID clientId,
  @NotBlank String currency,
  @NotNull AccountType type
) {}