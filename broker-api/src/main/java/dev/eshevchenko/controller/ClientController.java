package dev.eshevchenko.controller;

import dev.eshevchenko.doc.ClientExamples;
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
import dev.eshevchenko.i18n.swagger.annotations.ApiClientCreated;
import dev.eshevchenko.service.AccountService;
import dev.eshevchenko.service.ClientService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
  private final ClientService clientService;
  private final AccountService accountService;

  @PostMapping
  @ApiClientCreated(
    summary = "Создать клиента",
    description = "Регистрирует нового клиента брокера."
  )
  public ResponseEntity<CreateClientResponse> addClient(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(examples = @ExampleObject(value = ClientExamples.CREATE_CLIENT_REQUEST)))
    @Valid @RequestBody CreateClientRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
      .body(clientService.addClient(request));
  }

  @GetMapping("/{clientId}")
  public ResponseEntity<ClientResponse> getClient(@PathVariable String clientId) {
    return ResponseEntity.ok(clientService.getClient(clientId));
  }

  @PostMapping("/search")
  public ResponseEntity<PageResponse<ClientShortResponse>> searchClients(
    @Valid @RequestBody SearchClientRequest request) {
    return ResponseEntity.ok(clientService.searchClients(request));
  }

  @PutMapping("/{clientId}")
  public ResponseEntity<ClientResponse> updateClient(@PathVariable String clientId,
    @Valid @RequestBody UpdateClientRequest request) {
    return ResponseEntity.ok(clientService.updateClient(clientId, request));
  }

  @PatchMapping("/{clientId}")
  public ResponseEntity<ClientResponse> patchClient(@PathVariable String clientId,
    @RequestBody PatchClientRequest request) {
    return ResponseEntity.ok(clientService.patchClient(clientId, request));
  }

  @PostMapping("/{clientId}/block")
  public ResponseEntity<Void> blockClient(
    @PathVariable String clientId,
    @Valid @RequestBody BlockClientRequest request) {
    clientService.blockClient(clientId, request);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{clientId}/unblock")
  public ResponseEntity<Void> unblockClient(@PathVariable String clientId) {
    clientService.unblockClient(clientId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{clientId}/accounts")
  public ResponseEntity<List<AccountResponse>> getClientAccounts(
    @PathVariable String clientId) {
    return ResponseEntity.ok(accountService.getClientAccounts(clientId));
  }
}