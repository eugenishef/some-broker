package dev.eshevchenko.kafka;

import java.util.UUID;

public record ReportGenerationRequestedEvent(UUID reportId) {
}