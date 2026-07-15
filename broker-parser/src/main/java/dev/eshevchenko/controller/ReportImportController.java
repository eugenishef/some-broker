package dev.eshevchenko.controller;

import dev.eshevchenko.dto.CreateReportResponse;
import dev.eshevchenko.service.ReportImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/reports/import")
@RequiredArgsConstructor
public class ReportImportController {

  private final ReportImportService reportImportService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CreateReportResponse> importReport(@RequestParam MultipartFile file) {
    return ResponseEntity.status(HttpStatus.CREATED).body(reportImportService.importReport(file));
  }
}