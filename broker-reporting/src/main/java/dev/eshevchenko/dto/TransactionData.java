package dev.eshevchenko.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionData(
  UUID id,
  BigDecimal amount,
  String currency,
  String status,
  Instant createdAt
) {}