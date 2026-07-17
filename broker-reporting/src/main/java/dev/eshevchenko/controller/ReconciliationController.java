package dev.eshevchenko.controller;

import static dev.eshevchenko.doc.constants.ReconciliationConstants.GET_RECONCILIATION_RESULT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.GET_RECONCILIATION_RESULT_SUMMARY;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.GET_RECONCILIATION_STATUS_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.GET_RECONCILIATION_STATUS_SUMMARY;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.START_RECONCILIATION_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.START_RECONCILIATION_SUMMARY;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.TAG_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReconciliationConstants.TAG_NAME;

import dev.eshevchenko.dto.request.StartReconciliationRequest;
import dev.eshevchenko.dto.response.ReconciliationResultResponse;
import dev.eshevchenko.dto.response.ReconciliationStatusResponse;
import dev.eshevchenko.dto.response.StartReconciliationResponse;
import dev.eshevchenko.service.ReconciliationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
@Slf4j
@RestController
@RequestMapping("/api/v1/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {

  private final ReconciliationService reconciliationService;

  @PostMapping
  @Operation(summary = START_RECONCILIATION_SUMMARY, description = START_RECONCILIATION_DESCRIPTION)
  public ResponseEntity<StartReconciliationResponse> startReconciliation(
    @Valid @RequestBody StartReconciliationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(reconciliationService.startReconciliation(request));
  }

  @GetMapping("/{id}")
  @Operation(summary = GET_RECONCILIATION_STATUS_SUMMARY, description = GET_RECONCILIATION_STATUS_DESCRIPTION)
  public ResponseEntity<ReconciliationStatusResponse> getStatus(@PathVariable UUID id) {
    return ResponseEntity.ok(reconciliationService.getStatus(id));
  }

  @GetMapping("/{id}/result")
  @Operation(summary = GET_RECONCILIATION_RESULT_SUMMARY, description = GET_RECONCILIATION_RESULT_DESCRIPTION)
  public ResponseEntity<ReconciliationResultResponse> getResult(@PathVariable UUID id) {
    return ResponseEntity.ok(reconciliationService.getResult(id));
  }
}