package dev.eshevchenko.mapper;


import dev.eshevchenko.dto.PassportDto;
import dev.eshevchenko.dto.request.CreateClientRequest;
import dev.eshevchenko.dto.request.PatchClientRequest;
import dev.eshevchenko.dto.request.UpdateClientRequest;
import dev.eshevchenko.dto.response.AccountResponse;
import dev.eshevchenko.dto.response.ClientResponse;
import dev.eshevchenko.dto.response.ClientShortResponse;
import dev.eshevchenko.entity.Client;
import dev.eshevchenko.entity.Passport;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface ClientMapper {

  ClientResponse toResponse(Client entity, List<AccountResponse> accounts);

  @Mapping(source = "id", target = "clientId")
  ClientShortResponse toShortResponse(Client entity);

  List<ClientShortResponse> toShortResponse(List<Client> entities);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", constant = "ACTIVE")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "blockReason", ignore = true)
  @Mapping(target = "passport", source = "passport")
  Client toEntity(CreateClientRequest request);

  Passport toPassport(PassportDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patch(@MappingTarget Client entity, PatchClientRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "passport", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "blockReason", ignore = true)
  void update(@MappingTarget Client entity, UpdateClientRequest request);
}