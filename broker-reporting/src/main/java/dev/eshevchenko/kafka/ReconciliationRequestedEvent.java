package dev.eshevchenko.kafka;

import java.util.UUID;

public record ReconciliationRequestedEvent(UUID reconciliationId) {
}