package dev.eshevchenko.exception;

public class ReportImportException extends RuntimeException {

  public ReportImportException(String message) {
    super(message);
  }

  public ReportImportException(String message, Throwable cause) {
    super(message, cause);
  }
}