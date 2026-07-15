package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;

public record CreateClientResponse(
  String clientId,
  Status status
){}