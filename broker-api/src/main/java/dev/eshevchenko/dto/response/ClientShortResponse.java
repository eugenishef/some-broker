package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.ClientStatus;
import java.util.UUID;

public record ClientShortResponse(
  UUID clientId,
  String firstName,
  String lastName,
  String email,
  ClientStatus status
) {}