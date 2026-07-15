package dev.eshevchenko.validation;

import dev.eshevchenko.validation.annotations.Inn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class InnValidator implements ConstraintValidator<Inn, String> {
  private static final Pattern PATTERN = Pattern.compile("^\\d{10}(\\d{2})?$");

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || PATTERN.matcher(value).matches();
  }
}