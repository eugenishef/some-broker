package dev.eshevchenko.service;

import dev.eshevchenko.dto.request.StartReconciliationRequest;
import dev.eshevchenko.dto.response.ReconciliationResultResponse;
import dev.eshevchenko.dto.response.ReconciliationStatusResponse;
import dev.eshevchenko.dto.response.StartReconciliationResponse;
import java.util.UUID;

public interface ReconciliationService {

  default StartReconciliationResponse startReconciliation(StartReconciliationRequest request) {
    throw new UnsupportedOperationException();
  }

  default ReconciliationStatusResponse getStatus(UUID id) {
    throw new UnsupportedOperationException();
  }

  default ReconciliationResultResponse getResult(UUID id) {
    throw new UnsupportedOperationException();
  }
}