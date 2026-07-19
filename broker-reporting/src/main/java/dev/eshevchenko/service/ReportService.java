package dev.eshevchenko.service;

import dev.eshevchenko.dto.request.CreateReportRequest;
import dev.eshevchenko.dto.request.DisputeRequest;
import dev.eshevchenko.dto.request.ReportSearchRequest;
import dev.eshevchenko.dto.request.SupplementReportRequest;
import dev.eshevchenko.dto.response.CreateReportResponse;
import dev.eshevchenko.dto.response.DisputeResponse;
import dev.eshevchenko.dto.response.ReportResponse;
import dev.eshevchenko.dto.response.ReportVersionResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ReportService {

  CreateReportResponse createReport(CreateReportRequest request);

  Page<ReportResponse> searchReports(ReportSearchRequest request);

  ReportResponse getReport(UUID reportId);

  ReportResponse getOrCreateForPeriod(UUID clientId, LocalDate from, LocalDate to);

  List<ReportVersionResponse> getVersions(UUID reportId);

//  ReportVersionResponse getVersion(UUID reportId, int versionNumber);

  byte[] getVersion(UUID id, int versionNumber);

  byte[] getPdf(UUID reportId);

  ReportResponse supplementReport(UUID reportId, SupplementReportRequest request);

  DisputeResponse disputeOperations(UUID reportId, DisputeRequest request);
}