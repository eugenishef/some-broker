package dev.eshevchenko.exceptions;

import org.springframework.http.HttpStatus;

public class EntityConflictException extends ApiException {
  public EntityConflictException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}