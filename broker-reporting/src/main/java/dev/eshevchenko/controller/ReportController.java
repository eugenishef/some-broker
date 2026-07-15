package dev.eshevchenko.controller;

import dev.eshevchenko.dto.request.CreateReportRequest;
import dev.eshevchenko.dto.request.ReportSearchRequest;
import dev.eshevchenko.dto.response.CreateReportResponse;
import dev.eshevchenko.dto.response.ReportResponse;
import dev.eshevchenko.dto.response.ReportVersionResponse;
import dev.eshevchenko.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @PostMapping
  public ResponseEntity<CreateReportResponse> createReport(
    @Valid @RequestBody CreateReportRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(reportService.createReport(request));
  }

  @PutMapping("/search")
  @Operation(summary = "Поиск отчетов")
  public ResponseEntity<Page<ReportResponse>> searchReports(
    @Valid @RequestBody ReportSearchRequest request) {
    return ResponseEntity.ok(reportService.searchReports(request));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Получить отчет")
  public ResponseEntity<ReportResponse> getReport(@PathVariable UUID id) {
    return ResponseEntity.ok(reportService.getReport(id));
  }

  @GetMapping("/{id}/versions")
  @Operation(summary = "Получить версии отчета")
  public ResponseEntity<List<ReportVersionResponse>> getVersions(@PathVariable UUID id) {
    return ResponseEntity.ok(reportService.getVersions(id));
  }

  @GetMapping("/{id}/versions/{version}")
  @Operation(summary = "Получить конкретную версию отчета")
  public ResponseEntity<ReportVersionResponse> getVersion(
    @PathVariable UUID id, @PathVariable int version) {
    return ResponseEntity.ok(reportService.getVersion(id, version));
  }

  @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  @Operation(summary = "Скачать PDF отчета")
  public ResponseEntity<byte[]> getPdf(@PathVariable UUID id) {
    byte[] pdf = reportService.getPdf(id);
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report-" + id + ".pdf\"")
      .body(pdf);
  }
}