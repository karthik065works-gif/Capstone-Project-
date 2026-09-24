package com.icici.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dto.AccountRequest;
import com.icici.dto.AccountResponse;
import com.icici.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(
            AccountService accountService) {

        this.accountService = accountService;
    }

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable Long customerId,
            @Valid @RequestBody AccountRequest request) {

        AccountResponse account =
                accountService.createAccount(
                        customerId,
                        request
                );

        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @PathVariable String accountNumber) {

        AccountResponse account =
                accountService.getAccountByNumber(
                        accountNumber
                );

        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountRequest request) {

        AccountResponse account =
                accountService.updateAccount(
                        accountNumber,
                        request
                );

        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{accountNumber}/activate")
    public ResponseEntity<AccountResponse> activateAccount(
            @PathVariable String accountNumber) {

        AccountResponse account =
                accountService.activateAccount(
                        accountNumber
                );

        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{accountNumber}/deactivate")
    public ResponseEntity<AccountResponse> deactivateAccount(
            @PathVariable String accountNumber) {

        AccountResponse account =
                accountService.deactivateAccount(
                        accountNumber
                );

        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> closeAccount(
            @PathVariable String accountNumber) {

        AccountResponse account =
                accountService.closeAccount(
                        accountNumber
                );

        return ResponseEntity.ok(account);
    }
}