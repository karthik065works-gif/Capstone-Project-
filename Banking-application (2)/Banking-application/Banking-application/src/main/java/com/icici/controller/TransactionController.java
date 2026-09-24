package com.icici.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dto.TransactionRequest;
import com.icici.dto.TransactionResponse;
import com.icici.dto.TransferRequest;
import com.icici.model.TransactionType;
import com.icici.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService =
                transactionService;
    }

    // =========================================================
    // DEPOSIT
    // =========================================================

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transaction =
                transactionService.deposit(
                        accountNumber,
                        request
                );

        return ResponseEntity.ok(transaction);
    }

    // =========================================================
    // WITHDRAW
    // =========================================================

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transaction =
                transactionService.withdraw(
                        accountNumber,
                        request
                );

        return ResponseEntity.ok(transaction);
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    @PostMapping("/{accountNumber}/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransferRequest request) {

        TransactionResponse transaction =
                transactionService.transfer(
                        accountNumber,
                        request
                );

        return ResponseEntity.ok(transaction);
    }

    // =========================================================
    // GET ALL TRANSACTIONS
    // =========================================================

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>>
    getTransactionHistory(
            @PathVariable String accountNumber) {

        List<TransactionResponse> transactions =
                transactionService.getTransactionHistory(
                        accountNumber
                );

        return ResponseEntity.ok(transactions);
    }

    // =========================================================
    // GET TRANSACTIONS BY TYPE
    // =========================================================

    @GetMapping("/{accountNumber}/transactions/filter")
    public ResponseEntity<List<TransactionResponse>>
    getTransactionsByType(
            @PathVariable String accountNumber,
            @RequestParam TransactionType type) {

        List<TransactionResponse> transactions =
                transactionService.getTransactionsByType(
                        accountNumber,
                        type
                );

        return ResponseEntity.ok(transactions);
    }

    // =========================================================
    // GET TRANSACTION BY REFERENCE
    // =========================================================

    @GetMapping("/transactions/{transactionReference}")
    public ResponseEntity<TransactionResponse>
    getTransactionByReference(
            @PathVariable String transactionReference) {

        TransactionResponse transaction =
                transactionService.getTransactionByReference(
                        transactionReference
                );

        return ResponseEntity.ok(transaction);
    }
}