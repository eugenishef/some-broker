package dev.eshevchenko.service;

import dev.eshevchenko.dto.request.StartReconciliationRequest;
import dev.eshevchenko.dto.response.ReconciliationResultResponse;
import dev.eshevchenko.dto.response.ReconciliationStatusResponse;
import dev.eshevchenko.dto.response.StartReconciliationResponse;
import java.util.UUID;

public interface ReconciliationService {

  StartReconciliationResponse startReconciliation(StartReconciliationRequest request);

  ReconciliationStatusResponse getStatus(UUID id);

  ReconciliationResultResponse getResult(UUID id);
}