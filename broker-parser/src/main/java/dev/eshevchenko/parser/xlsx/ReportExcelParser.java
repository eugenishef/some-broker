package dev.eshevchenko.parser.xlsx;

import dev.eshevchenko.dto.ReportImportData;
import org.springframework.web.multipart.MultipartFile;

public interface ReportExcelParser {
  ReportImportData parse(MultipartFile file);
}