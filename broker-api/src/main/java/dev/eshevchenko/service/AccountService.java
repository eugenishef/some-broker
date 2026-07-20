package dev.eshevchenko.service;

import dev.eshevchenko.dto.request.CreateAccountRequest;
import dev.eshevchenko.dto.response.AccountResponse;
import dev.eshevchenko.dto.response.CreateAccountResponse;
import java.util.List;
import java.util.UUID;

public interface AccountService {
  CreateAccountResponse createAccount(CreateAccountRequest request);

  List<AccountResponse> getClientAccounts(UUID clientId);

  void closeAccount(UUID accountId);

  void freezeAccount(UUID accountId);
}