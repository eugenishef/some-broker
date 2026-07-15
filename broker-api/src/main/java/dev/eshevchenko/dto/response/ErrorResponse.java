package dev.eshevchenko.dto.response;

public record ErrorResponse(
  String code,
  String message
) {}