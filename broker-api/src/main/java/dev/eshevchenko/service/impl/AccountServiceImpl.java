package dev.eshevchenko.service.impl;

import dev.eshevchenko.dao.AccountRepository;
import dev.eshevchenko.dao.ClientRepository;
import dev.eshevchenko.dto.request.CreateAccountRequest;
import dev.eshevchenko.dto.response.AccountResponse;
import dev.eshevchenko.dto.response.CreateAccountResponse;
import dev.eshevchenko.enums.AccountStatus;
import dev.eshevchenko.exceptions.EntityNotFoundException;
import dev.eshevchenko.mapper.AccountMapper;
import dev.eshevchenko.entity.Account;
import dev.eshevchenko.service.AccountService;
import dev.eshevchenko.utils.AccountUtils;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final ClientRepository clientRepository;
  private final AccountMapper accountMapper;
  private final AccountUtils accountUtils;

  @Override
  @Transactional
  public CreateAccountResponse createAccount(CreateAccountRequest request) {
    UUID clientId;

    try {
      clientId = UUID.fromString(request.clientId());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Некорректный UUID клиента: " + request.clientId());
    }

    if (!clientRepository.existsById(clientId)) {
      throw new EntityNotFoundException("Клиент с id=" + clientId + " не найден");
    }

    Account entity = accountMapper.toEntity(request, clientId);
    entity.setAccountNumber(accountUtils.generateAccountNumber());

    Account saved = accountRepository.save(entity);
    return accountMapper.toCreateResponse(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AccountResponse> getClientAccounts(String clientId) {
    UUID id;
    try {
      id = UUID.fromString(clientId);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Некорректный UUID клиента: " + clientId);
    }
    return accountMapper.toResponse(accountRepository.findAllByClientId(id));
  }

  @Override
  @Transactional
  public void closeAccount(String accountId) {
    Account entity = accountUtils.getOrThrow(accountId);
    entity.setStatus(AccountStatus.CLOSED);
    entity.setClosedAt(Instant.now());
    accountRepository.save(entity);
  }

  @Override
  @Transactional
  public void freezeAccount(String accountId) {
    Account entity = accountUtils.getOrThrow(accountId);
    entity.setStatus(AccountStatus.FROZEN);
    accountRepository.save(entity);
  }
}