package dev.eshevchenko.validation;

import dev.eshevchenko.validation.annotations.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private static final Pattern PATTERN = Pattern.compile(
    "^(\\+7|8)?\\s?\\(?(\\d{3})\\)?\\s?\\d{3}[\\s.-]?\\d{2}[\\s.-]?\\d{2}$"
  );

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    String cleaned = value.replaceAll("\\s+", "");
    return PATTERN.matcher(cleaned).matches();
  }
}