package dev.eshevchenko.controller;

import dev.eshevchenko.dto.request.StartReconciliationRequest;
import dev.eshevchenko.dto.response.ReconciliationResultResponse;
import dev.eshevchenko.dto.response.ReconciliationStatusResponse;
import dev.eshevchenko.dto.response.StartReconciliationResponse;
import dev.eshevchenko.service.ReconciliationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {

  private final ReconciliationService reconciliationService;

  @PostMapping
  @Operation(summary = "Запустить сверку")
  public ResponseEntity<StartReconciliationResponse> startReconciliation(
    @Valid @RequestBody StartReconciliationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(reconciliationService.startReconciliation(request));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить статус сверки")
  public ResponseEntity<ReconciliationStatusResponse> getStatus(@PathVariable UUID id) {
    return ResponseEntity.ok(reconciliationService.getStatus(id));
  }

  @GetMapping("/{id}/result")
  @Operation(summary = "Получить различия сверки")
  public ResponseEntity<ReconciliationResultResponse> getResult(@PathVariable UUID id) {
    return ResponseEntity.ok(reconciliationService.getResult(id));
  }
}