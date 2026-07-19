package dev.eshevchenko.controller;

import static dev.eshevchenko.doc.constants.ClientApiConstants.BLOCK_CLIENT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.BLOCK_CLIENT_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.CREATE_CLIENT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.CREATE_CLIENT_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.GET_CLIENT_ACCOUNTS_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.GET_CLIENT_ACCOUNTS_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.GET_CLIENT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.GET_CLIENT_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.PATCH_CLIENT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.PATCH_CLIENT_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.SEARCH_CLIENTS_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.SEARCH_CLIENTS_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.TAG_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.TAG_NAME;
import static dev.eshevchenko.doc.constants.ClientApiConstants.UNBLOCK_CLIENT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.UNBLOCK_CLIENT_SUMMARY;
import static dev.eshevchenko.doc.constants.ClientApiConstants.UPDATE_CLIENT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.ClientApiConstants.UPDATE_CLIENT_SUMMARY;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
@Validated
@RestController
@RequestMapping("${app.api.base-path}/clients")
@RequiredArgsConstructor
public class ClientController {
  private final ClientService clientService;
  private final AccountService accountService;

  @PostMapping
  @Operation(summary = CREATE_CLIENT_SUMMARY, description = CREATE_CLIENT_DESCRIPTION)
  public ResponseEntity<CreateClientResponse> addClient(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(examples = @ExampleObject(value = ClientExamples.CREATE_CLIENT_REQUEST)))
    @Valid @RequestBody CreateClientRequest request) {

    return ResponseEntity.status(HttpStatus.CREATED)
      .body(clientService.addClient(request));
  }

  @GetMapping("/{clientId}")
  @Operation(summary = GET_CLIENT_SUMMARY, description = GET_CLIENT_DESCRIPTION)
  public ResponseEntity<ClientResponse> getClient(@PathVariable String clientId) {
    return ResponseEntity.ok(clientService.getClient(clientId));
  }

  @PostMapping("/search")
  @Operation(summary = SEARCH_CLIENTS_SUMMARY, description = SEARCH_CLIENTS_DESCRIPTION)
  public ResponseEntity<PageResponse<ClientShortResponse>> searchClients(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(examples = @ExampleObject(value = ClientExamples.SEARCH_CLIENT_REQUEST)))
    @Valid @RequestBody SearchClientRequest request) {
    return ResponseEntity.ok(clientService.searchClients(request));
  }

  @PutMapping("/{clientId}")
  @Operation(summary = UPDATE_CLIENT_SUMMARY, description = UPDATE_CLIENT_DESCRIPTION)
  public ResponseEntity<ClientResponse> updateClient(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(examples = @ExampleObject(value = ClientExamples.UPDATE_CLIENT_REQUEST)))
    @PathVariable String clientId,
    @Valid @RequestBody UpdateClientRequest request) {
    return ResponseEntity.ok(clientService.updateClient(clientId, request));
  }

  @PatchMapping("/{clientId}")
  @Operation(summary = PATCH_CLIENT_SUMMARY, description = PATCH_CLIENT_DESCRIPTION)
  public ResponseEntity<ClientResponse> patchClient(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(examples = @ExampleObject(value = ClientExamples.PATCH_CLIENT_REQUEST)))
    @PathVariable String clientId,
    @RequestBody PatchClientRequest request) {
    return ResponseEntity.ok(clientService.patchClient(clientId, request));
  }

  @PostMapping("/{clientId}/block")
  @Operation(summary = BLOCK_CLIENT_SUMMARY, description = BLOCK_CLIENT_DESCRIPTION)
  public ResponseEntity<Void> blockClient(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(examples = @ExampleObject(value = ClientExamples.BLOCK_CLIENT_REQUEST)))
    @PathVariable String clientId,
    @Valid @RequestBody BlockClientRequest request) {
    clientService.blockClient(clientId, request);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{clientId}/unblock")
  @Operation(summary = UNBLOCK_CLIENT_SUMMARY, description = UNBLOCK_CLIENT_DESCRIPTION)
  public ResponseEntity<Void> unblockClient(@PathVariable String clientId) {
    clientService.unblockClient(clientId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{clientId}/accounts")
  @Operation(summary = GET_CLIENT_ACCOUNTS_SUMMARY, description = GET_CLIENT_ACCOUNTS_DESCRIPTION)
  public ResponseEntity<List<AccountResponse>> getClientAccounts(
    @PathVariable String clientId) {
    return ResponseEntity.ok(accountService.getClientAccounts(clientId));
  }
}