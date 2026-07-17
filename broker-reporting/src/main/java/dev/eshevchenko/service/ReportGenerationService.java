package dev.eshevchenko.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ReportGenerationService {

  CompletableFuture<Void> generate(UUID reportId);
}