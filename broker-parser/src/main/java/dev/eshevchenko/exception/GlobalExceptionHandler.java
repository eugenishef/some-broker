package dev.eshevchenko.exception;

import dev.eshevchenko.dto.ApiErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    log.warn("Нарушение целостности данных: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(new ApiErrorResponse(
        HttpStatus.CONFLICT.value(),
        "Конфликт данных: запись с такими значениями уже существует",
        List.of()));
  }
}