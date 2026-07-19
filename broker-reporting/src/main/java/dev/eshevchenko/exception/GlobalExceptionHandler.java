package dev.eshevchenko.exception;


import dev.eshevchenko.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

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

//  @ExceptionHandler(RuntimeException.class)
//  public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
//    request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
//    log.warn("Ресурс не найден: {}", ex.getMessage());
//    return ResponseEntity.status(HttpStatus.NOT_FOUND)
//      .body(new ErrorResponse(ex.getMessage()));
//  }
}