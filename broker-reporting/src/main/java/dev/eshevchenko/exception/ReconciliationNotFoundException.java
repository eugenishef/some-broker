package dev.eshevchenko.exception;

public class ReconciliationNotFoundException extends RuntimeException {
  public ReconciliationNotFoundException(String message) {
    super(message);
  }
}