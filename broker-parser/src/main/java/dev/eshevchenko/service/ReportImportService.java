package dev.eshevchenko.service;

import dev.eshevchenko.dto.event.CreateReportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ReportImportService {

  CreateReportResponse importReport(MultipartFile file);
}