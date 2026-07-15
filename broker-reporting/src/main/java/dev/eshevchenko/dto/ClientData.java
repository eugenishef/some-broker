package dev.eshevchenko.dto;

import java.util.UUID;

public record ClientData(
  UUID id,
  String fullName,
  String inn,
  String email
) {}