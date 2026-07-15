package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.ClientStatus;
import java.time.LocalDate;
import java.util.List;

public record ClientResponse(
  String id,
  String firstName,
  String lastName,
  String middleName,
  LocalDate birthDate,
  String email,
  String phone,
  ClientStatus status,
  List<AccountResponse> accounts
) {}