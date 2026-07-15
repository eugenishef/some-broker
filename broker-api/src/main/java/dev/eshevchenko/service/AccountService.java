package dev.eshevchenko.service;

import dev.eshevchenko.dto.request.CreateAccountRequest;
import dev.eshevchenko.dto.response.AccountResponse;
import dev.eshevchenko.dto.response.CreateAccountResponse;
import java.util.List;

public interface AccountService {
  CreateAccountResponse createAccount(CreateAccountRequest request);
  List<AccountResponse> getClientAccounts(String clientId);
  void closeAccount(String accountId);
  void freezeAccount(String accountId);
}