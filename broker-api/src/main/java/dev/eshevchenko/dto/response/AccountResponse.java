package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.AccountStatus;
import dev.eshevchenko.enums.AccountType;
import java.time.Instant;

public record AccountResponse(
  String accountId,
  String number,
  String currency,
  AccountType type,
  AccountStatus status,
  Instant openedAt,
  Instant closedAt
) {}