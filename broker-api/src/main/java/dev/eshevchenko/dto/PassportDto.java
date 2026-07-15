package dev.eshevchenko.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassportDto {

  @NotBlank
  @Size(min = 4, max = 4)
  String series;

  @NotBlank
  @Size(min = 6, max = 6)
  String number;
}