package dev.eshevchenko.dto.response;

import java.util.List;

public record MeResponse(
  String clientId,
  List<String> roles
) {}