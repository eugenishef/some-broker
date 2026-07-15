package dev.eshevchenko.service;

import dev.eshevchenko.dto.request.BlockClientRequest;
import dev.eshevchenko.dto.request.CreateClientRequest;
import dev.eshevchenko.dto.request.PatchClientRequest;
import dev.eshevchenko.dto.request.SearchClientRequest;
import dev.eshevchenko.dto.request.UpdateClientRequest;
import dev.eshevchenko.dto.response.ClientResponse;
import dev.eshevchenko.dto.response.ClientShortResponse;
import dev.eshevchenko.dto.response.CreateClientResponse;
import dev.eshevchenko.dto.response.PageResponse;

public interface ClientService {
  CreateClientResponse addClient(CreateClientRequest request);
  ClientResponse getClient(String clientId);
  PageResponse<ClientShortResponse> searchClients(SearchClientRequest request);
  ClientResponse updateClient(String clientId, UpdateClientRequest request);
  ClientResponse patchClient(String clientId, PatchClientRequest request);
  void blockClient(String clientId, BlockClientRequest request);
  void unblockClient(String clientId);
}