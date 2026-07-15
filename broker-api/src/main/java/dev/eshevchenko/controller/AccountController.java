package dev.eshevchenko.controller;

import dev.eshevchenko.dto.request.CreateAccountRequest;
import dev.eshevchenko.dto.response.CreateAccountResponse;
import dev.eshevchenko.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Счета", description = "Управление брокерскими счетами клиентов")
public class AccountController {
  private final AccountService accountService;

  @PostMapping
  public ResponseEntity<CreateAccountResponse> createAccount(
    @Valid @RequestBody CreateAccountRequest request) {
    CreateAccountResponse response = accountService.createAccount(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/{accountId}/close")
  public ResponseEntity<Void> closeAccount(@PathVariable String accountId) {
    accountService.closeAccount(accountId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{accountId}/freeze")
  public ResponseEntity<Void> freezeAccount(@PathVariable String accountId) {
    accountService.freezeAccount(accountId);
    return ResponseEntity.noContent().build();
  }
}