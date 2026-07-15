package dev.eshevchenko.exception;

public class ReportVersionNotFoundException extends RuntimeException {
  public ReportVersionNotFoundException(String message) {
    super(message);
  }
}