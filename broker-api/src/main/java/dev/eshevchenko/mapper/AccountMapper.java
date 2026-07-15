package dev.eshevchenko.mapper;

import dev.eshevchenko.dto.request.CreateAccountRequest;
import dev.eshevchenko.dto.response.AccountResponse;
import dev.eshevchenko.dto.response.CreateAccountResponse;
import dev.eshevchenko.entity.Account;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(target = "accountId", expression = "java(entity.getId().toString())")
  @Mapping(target = "number", source = "accountNumber")
  AccountResponse toResponse(Account entity);

  List<AccountResponse> toResponse(List<Account> entities);

  @Mapping(target = "accountId", expression = "java(entity.getId().toString())")
  @Mapping(target = "number", source = "accountNumber")
  CreateAccountResponse toCreateResponse(Account entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "clientId", source = "clientId")
  @Mapping(target = "currency", source = "request.currency")
  @Mapping(target = "type", source = "request.type")
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "openedAt", ignore = true)
  @Mapping(target = "closedAt", ignore = true)
  @Mapping(target = "accountNumber", ignore = true)
  Account toEntity(CreateAccountRequest request, UUID clientId);
}