package dev.eshevchenko.service.impl;

import static dev.eshevchenko.exceptions.constants.ExMessageConstants.EMAIL_ALREADY_EXISTS;
import static dev.eshevchenko.exceptions.constants.ExMessageConstants.INN_ALREADY_EXISTS;

import dev.eshevchenko.dao.AccountRepository;
import dev.eshevchenko.dao.ClientRepository;
import dev.eshevchenko.dto.request.BlockClientRequest;
import dev.eshevchenko.dto.request.CreateClientRequest;
import dev.eshevchenko.dto.request.PatchClientRequest;
import dev.eshevchenko.dto.request.SearchClientRequest;
import dev.eshevchenko.dto.request.UpdateClientRequest;
import dev.eshevchenko.dto.response.AccountResponse;
import dev.eshevchenko.dto.response.ClientResponse;
import dev.eshevchenko.dto.response.ClientShortResponse;
import dev.eshevchenko.dto.response.CreateClientResponse;
import dev.eshevchenko.dto.response.PageResponse;
import dev.eshevchenko.entity.Account;
import dev.eshevchenko.enums.ClientStatus;
import dev.eshevchenko.enums.Status;
import dev.eshevchenko.exceptions.EntityConflictException;
import dev.eshevchenko.mapper.AccountMapper;
import dev.eshevchenko.mapper.ClientMapper;
import dev.eshevchenko.entity.Client;
import dev.eshevchenko.service.ClientService;
import dev.eshevchenko.utils.ClientUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

  private final ClientRepository repository;
  private final AccountRepository accountRepository;
  private final ClientMapper clientMapper;
  private final AccountMapper accountMapper;
  private final ClientUtils clientUtils;

  @Override
  @Transactional
  public CreateClientResponse addClient(CreateClientRequest request) {
    if (repository.existsByInn(request.getInn())) {
      throw new EntityConflictException(INN_ALREADY_EXISTS);
    }
    if (repository.existsByEmail(request.getEmail())) {
      throw new EntityConflictException(EMAIL_ALREADY_EXISTS);
    }

    Client entity = clientMapper.toEntity(request);
    Client saved = repository.save(entity);
    return new CreateClientResponse(saved.getId(), Status.CREATED);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "clients", key = "#clientId")
  public ClientResponse getClient(String clientId) {
    Client entity = clientUtils.getOrThrow(clientId);
    List<Account> findClients = accountRepository.findAllByClientId(entity.getId());

    List<AccountResponse> accounts = accountMapper.toResponse(findClients);
    return clientMapper.toResponse(entity, accounts);
  }


  @Override
  @Transactional(readOnly = true)
  public PageResponse<ClientShortResponse> searchClients(SearchClientRequest request) {
    Sort sort = Sort.unsorted();
    for (SearchClientRequest.SortField field : request.getSort()) {
      sort = sort.and(Sort.by(field.getDirection(), field.getField()));
    }
    Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

    String email = request.getFilter() != null ? request.getFilter().getEmail() : null;
    ClientStatus status = request.getFilter() != null ? request.getFilter().getStatus() : null;

    Page<ClientShortResponse> page = repository.search(email, status, pageable)
      .map(clientMapper::toShortResponse);
    return PageResponse.of(page);
  }

  @Override
  @Transactional
  @CacheEvict(value = "clients", key = "#clientId")
  public ClientResponse updateClient(String clientId, UpdateClientRequest request) {
    Client entity = clientUtils.getOrThrow(clientId);
    clientMapper.update(entity, request);

    Client saved = repository.save(entity);
    List<AccountResponse> accounts =
      accountMapper.toResponse(accountRepository.findAllByClientId(saved.getId()));
    return clientMapper.toResponse(saved, accounts);
  }

  @Override
  @Transactional
  @CacheEvict(value = "clients", key = "#clientId")
  public ClientResponse patchClient(String clientId, PatchClientRequest request) {

    Client entity = clientUtils.getOrThrow(clientId);
    clientMapper.patch(entity, request);

    Client saved = repository.save(entity);
    List<Account> findClients = accountRepository.findAllByClientId(saved.getId());
    List<AccountResponse> accounts = accountMapper.toResponse(findClients);
    return clientMapper.toResponse(saved, accounts);
  }

  @Override
  @Transactional
  @CacheEvict(value = "clients", key = "#clientId")
  public void blockClient(String clientId, BlockClientRequest request) {
    Client entity = clientUtils.getOrThrow(clientId);
    entity.setStatus(ClientStatus.BLOCKED);
    entity.setBlockReason(request.reason());
    repository.save(entity);
  }

  @Override
  @Transactional
  @CacheEvict(value = "clients", key = "#clientId")
  public void unblockClient(String clientId) {
    Client entity = clientUtils.getOrThrow(clientId);
    entity.setStatus(ClientStatus.ACTIVE);
    entity.setBlockReason(null);
    repository.save(entity);
  }
}