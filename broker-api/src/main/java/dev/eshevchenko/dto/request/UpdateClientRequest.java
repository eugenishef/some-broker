package dev.eshevchenko.dto.request;

import dev.eshevchenko.validation.annotations.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateClientRequest {

  @NotBlank
  String firstName;

  @NotBlank
  String lastName;

  String middleName;

  @Past
  LocalDate birthDate;

  @NotBlank
  @Email
  String email;

  @PhoneNumber
  String phone;
}