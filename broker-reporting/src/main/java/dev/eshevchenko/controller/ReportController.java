package dev.eshevchenko.controller;

import static dev.eshevchenko.doc.constants.ReportConstants.CREATE_REPORT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.CREATE_REPORT_SUMMARY;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_PDF_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_PDF_SUMMARY;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_SUMMARY;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_VERSIONS_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_VERSIONS_SUMMARY;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_VERSION_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.GET_REPORT_VERSION_SUMMARY;
import static dev.eshevchenko.doc.constants.ReportConstants.SEARCH_REPORTS_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.SEARCH_REPORTS_SUMMARY;
import static dev.eshevchenko.doc.constants.ReportConstants.TAG_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ReportConstants.TAG_NAME;

import dev.eshevchenko.dto.request.CreateReportRequest;
import dev.eshevchenko.dto.request.ReportSearchRequest;
import dev.eshevchenko.dto.response.CreateReportResponse;
import dev.eshevchenko.dto.response.ReportResponse;
import dev.eshevchenko.dto.response.ReportVersionResponse;
import dev.eshevchenko.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportService reportService;

  @PostMapping
  @Operation(summary = CREATE_REPORT_SUMMARY, description = CREATE_REPORT_DESCRIPTION)
  public ResponseEntity<CreateReportResponse> createReport(
    @Valid @RequestBody CreateReportRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(reportService.createReport(request));
  }

  @PutMapping("/search")
  @Operation(summary = SEARCH_REPORTS_SUMMARY, description = SEARCH_REPORTS_DESCRIPTION)
  public ResponseEntity<Page<ReportResponse>> searchReports(
    @Valid @RequestBody ReportSearchRequest request) {
    return ResponseEntity.ok(reportService.searchReports(request));
  }

  @GetMapping("/{id}")
  @Operation(summary = GET_REPORT_SUMMARY, description = GET_REPORT_DESCRIPTION)
  public ResponseEntity<ReportResponse> getReport(@PathVariable UUID id) {
    return ResponseEntity.ok(reportService.getReport(id));
  }

  @GetMapping("/{id}/versions")
  @Operation(summary = GET_REPORT_VERSIONS_SUMMARY, description = GET_REPORT_VERSIONS_DESCRIPTION)
  public ResponseEntity<List<ReportVersionResponse>> getVersions(@PathVariable UUID id) {
    return ResponseEntity.ok(reportService.getVersions(id));
  }

//  @GetMapping("/{id}/versions/{version}")
//  @Operation(summary = GET_REPORT_VERSION_SUMMARY, description = GET_REPORT_VERSION_DESCRIPTION)
//  public ResponseEntity<ReportVersionResponse> getVersion(
//    @PathVariable UUID id, @PathVariable int version) {
//    return ResponseEntity.ok(reportService.getVersion(id, version));
//  }

  @GetMapping(value = "/{id}/versions/{version}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  @Operation(summary = "GET_REPORT_VERSION_PDF_SUMMARY", description = "GET_REPORT_VERSION_PDF_DESCRIPTION")
  public ResponseEntity<byte[]> getVersionPdf(
    @PathVariable UUID id, @PathVariable int version) {
    byte[] pdf = reportService.getVersion(id, version);
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"report-" + id + "-v" + version + ".pdf\"")
      .body(pdf);
  }

  @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  @Operation(summary = GET_REPORT_PDF_SUMMARY, description = GET_REPORT_PDF_DESCRIPTION)
  public ResponseEntity<byte[]> getPdf(@PathVariable UUID id) {
    byte[] pdf = reportService.getPdf(id);
    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report-" + id + ".pdf\"")
      .body(pdf);
  }
}