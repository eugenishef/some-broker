package dev.eshevchenko.mapper;

import dev.eshevchenko.dto.ReportImportData;
import dev.eshevchenko.dto.ReportOperationDto;
import dev.eshevchenko.entity.Report;
import dev.eshevchenko.entity.ReportOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {
  ReportOperation toOperation(ReportOperationDto dto);

  @Mapping(target = "type", constant = "IMPORTED")
  @Mapping(target = "status", constant = "PROCESSING")
  Report toEntity(ReportImportData data);
}