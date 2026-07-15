package dev.eshevchenko.dto.response;

import dev.eshevchenko.enums.Status;

public record DisputeResponse(String disputeId, Status status) {}