package dev.eshevchenko.controller;

import static dev.eshevchenko.doc.constants.AccountApiConstants.CLOSE_ACCOUNT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.AccountApiConstants.CLOSE_ACCOUNT_SUMMARY;
import static dev.eshevchenko.doc.constants.AccountApiConstants.CREATE_ACCOUNT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.AccountApiConstants.CREATE_ACCOUNT_SUMMARY;
import static dev.eshevchenko.doc.constants.AccountApiConstants.FREEZE_ACCOUNT_DESCRIPTION;
import static dev.eshevchenko.doc.constants.AccountApiConstants.FREEZE_ACCOUNT_SUMMARY;
import static dev.eshevchenko.doc.constants.AccountApiConstants.TAG_DESCRIPTION;
import static dev.eshevchenko.doc.constants.AccountApiConstants.TAG_NAME;

import dev.eshevchenko.dto.request.CreateAccountRequest;
import dev.eshevchenko.dto.response.CreateAccountResponse;
import dev.eshevchenko.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = TAG_NAME, description = TAG_DESCRIPTION)
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;

  @PostMapping
  @Operation(summary = CREATE_ACCOUNT_SUMMARY, description = CREATE_ACCOUNT_DESCRIPTION)
  public ResponseEntity<CreateAccountResponse> createAccount(
    @Valid @RequestBody CreateAccountRequest request) {
    CreateAccountResponse response = accountService.createAccount(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/{accountId}/close")
  @Operation(summary = CLOSE_ACCOUNT_SUMMARY, description = CLOSE_ACCOUNT_DESCRIPTION)
  public ResponseEntity<Void> closeAccount(@PathVariable String accountId) {
    accountService.closeAccount(accountId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{accountId}/freeze")
  @Operation(summary = FREEZE_ACCOUNT_SUMMARY, description = FREEZE_ACCOUNT_DESCRIPTION)
  public ResponseEntity<Void> freezeAccount(@PathVariable String accountId) {
    accountService.freezeAccount(accountId);
    return ResponseEntity.noContent().build();
  }
}