package dev.eshevchenko.exception;


import dev.eshevchenko.dto.ApiErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    ReportNotFoundException.class,
    ReportVersionNotFoundException.class,
    ReconciliationNotFoundException.class
  })
  public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex) {
    log.warn("Ресурс не найден: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        List.of()
      ));
  }

  @ExceptionHandler(ReportRenderingException.class)
  public ResponseEntity<ApiErrorResponse> handleRenderingError(ReportRenderingException ex) {
    log.error("Ошибка рендеринга отчета", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Не удалось сформировать PDF отчета",
        List.of()));
  }
}