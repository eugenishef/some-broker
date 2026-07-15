package dev.eshevchenko.dto.response;

import java.util.List;

public record ReconciliationResultResponse(
  String id,
  List<dev.eshevchenko.dto.view.DiscrepancyView> discrepancies
) {}