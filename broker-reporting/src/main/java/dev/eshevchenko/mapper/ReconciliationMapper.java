package dev.eshevchenko.mapper;

import dev.eshevchenko.dto.request.StartReconciliationRequest;
import dev.eshevchenko.dto.response.ReconciliationStatusResponse;
import dev.eshevchenko.entity.Reconciliation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReconciliationMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Reconciliation toEntity(StartReconciliationRequest request);

  @Mapping(target = "id", expression = "java(reconciliation.getId().toString())")
  ReconciliationStatusResponse toStatusResponse(Reconciliation reconciliation);
}