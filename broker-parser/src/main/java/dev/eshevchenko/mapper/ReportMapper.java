package dev.eshevchenko.mapper;

import dev.eshevchenko.dto.ReportOperationDto;
import dev.eshevchenko.entity.ReportOperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
  ReportOperation toOperation(ReportOperationDto dto);
}