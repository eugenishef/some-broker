package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;
import java.util.UUID;

public record CreateClientResponse(
  UUID clientId,
  Status status
){}