package dev.eshevchenko.mapper;

import dev.eshevchenko.dto.request.CreateReportRequest;
import dev.eshevchenko.dto.response.ReportResponse;
import dev.eshevchenko.dto.response.ReportVersionResponse;
import dev.eshevchenko.entity.Report;
import dev.eshevchenko.entity.ReportVersionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "content", ignore = true)
  Report toEntity(CreateReportRequest request);

  @Mapping(target = "id", expression = "java(report.getId().toString())")
  @Mapping(target = "currentVersion", source = "version")
  ReportResponse toResponse(Report report);

  ReportVersionResponse toVersionResponse(ReportVersionEntity entity);
}