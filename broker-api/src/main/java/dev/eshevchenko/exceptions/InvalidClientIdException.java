package dev.eshevchenko.exceptions;

public class InvalidClientIdException extends RuntimeException {
  public InvalidClientIdException(String message) {
    super(message);
  }
}