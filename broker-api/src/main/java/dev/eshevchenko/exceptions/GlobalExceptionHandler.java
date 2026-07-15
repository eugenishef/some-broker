package dev.eshevchenko.exceptions;

import dev.eshevchenko.dto.ApiErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ClientAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleClientAlreadyExists(ClientAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
      .body(new ApiErrorResponse(
        HttpStatus.CONFLICT.value(),
        ex.getMessage(),
        List.of()
      ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
      .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
      .toList();

    log.warn("Ошибка валидации запроса: {}", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Ошибка валидации запроса",
        errors
      ));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleAnyException(Exception ex) {
    log.error("Непредвиденная ошибка", ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Внутренняя ошибка сервера",
        List.of()
      ));
  }

  @ExceptionHandler(InvalidClientIdException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidClientId(InvalidClientIdException ex) {
    return ResponseEntity.badRequest()
      .body(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), List.of()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
    log.warn("Некорректное тело запроса: {}", ex.getMessage());
    return ResponseEntity.badRequest()
      .body(new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Некорректный формат запроса. Проверьте типы и форматы полей (например, дата — в формате YYYY-MM-DD)",
        List.of()));
  }
}