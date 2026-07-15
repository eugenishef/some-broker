package dev.eshevchenko.validation.annotations;

import static dev.eshevchenko.i18n.messages.ValidationMessages.PHONE_INVALID;

import dev.eshevchenko.validation.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
  String message() default PHONE_INVALID;
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}