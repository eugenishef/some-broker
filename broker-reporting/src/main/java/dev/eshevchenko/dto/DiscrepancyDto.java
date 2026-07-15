package dev.eshevchenko.dto;

public record DiscrepancyDto(
  String field,
  String expected,
  String actual
) {}