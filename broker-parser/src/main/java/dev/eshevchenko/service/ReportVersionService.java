package dev.eshevchenko.service;

import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.entity.ReportVersionEntity;
import dev.eshevchenko.enums.ReportStatus;
import java.util.UUID;


public interface ReportVersionService {

  ReportVersionEntity saveNewVersion(
    UUID reportId, ReportStatus status, ReportImportData data);
}