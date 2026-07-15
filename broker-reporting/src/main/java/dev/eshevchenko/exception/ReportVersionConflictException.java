package dev.eshevchenko.exception;

public class ReportVersionConflictException extends RuntimeException {
  public ReportVersionConflictException(String message) { super(message); }
}