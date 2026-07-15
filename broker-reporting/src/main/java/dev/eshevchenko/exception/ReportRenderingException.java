package dev.eshevchenko.exception;

public class ReportRenderingException extends RuntimeException {
  public ReportRenderingException(String message, Throwable cause) {
    super(message, cause);
  }
}