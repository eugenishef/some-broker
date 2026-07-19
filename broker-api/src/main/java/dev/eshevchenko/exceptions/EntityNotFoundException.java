package dev.eshevchenko.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {
  public EntityNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}