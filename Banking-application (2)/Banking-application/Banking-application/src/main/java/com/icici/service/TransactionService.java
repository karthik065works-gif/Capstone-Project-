package com.icici.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dto.TransactionRequest;
import com.icici.dto.TransactionResponse;
import com.icici.dto.TransferRequest;
import com.icici.exception.AccountNotFoundException;
import com.icici.exception.AccountOperationException;
import com.icici.exception.TransactionNotFoundException;
import com.icici.model.Account;
import com.icici.model.AccountStatus;
import com.icici.model.NotificationType;
import com.icici.model.Transaction;
import com.icici.model.TransactionType;
import com.icici.repository.AccountRepository;
import com.icici.repository.TransactionRepository;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public TransactionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            NotificationService notificationService) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    // =========================================================
    // DEPOSIT
    // =========================================================

    @Transactional
    public TransactionResponse deposit(
            String accountNumber,
            TransactionRequest request) {

        Account account =
                getAccount(accountNumber);

        validateActiveAccount(account);
        validateAmount(request.getAmount());

        BigDecimal newBalance =
                account.getBalance()
                        .add(request.getAmount());

        account.setBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction =
                new Transaction();

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setAccount(account);

        transaction.setTransactionType(
                TransactionType.DEPOSIT
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setBalanceAfter(
                newBalance
        );

        transaction.setDescription(
                request.getDescription()
        );

        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        notificationService.createNotification(
                account.getCustomer().getId(),
                NotificationType.DEPOSIT,
                "₹" + request.getAmount()
                        + " deposited into account "
                        + account.getAccountNumber()
                        + ". Available balance: ₹"
                        + newBalance,
                savedTransaction.getTransactionReference()
        );

        return toResponse(savedTransaction);
    }

    // =========================================================
    // WITHDRAW
    // =========================================================

    @Transactional
    public TransactionResponse withdraw(
            String accountNumber,
            TransactionRequest request) {

        Account account =
                getAccount(accountNumber);

        validateActiveAccount(account);
        validateAmount(request.getAmount());

        if (account.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new AccountOperationException(
                    "Insufficient balance"
            );
        }

        BigDecimal newBalance =
                account.getBalance()
                        .subtract(request.getAmount());

        account.setBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction =
                new Transaction();

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setAccount(account);

        transaction.setTransactionType(
                TransactionType.WITHDRAWAL
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setBalanceAfter(
                newBalance
        );

        transaction.setDescription(
                request.getDescription()
        );

        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        notificationService.createNotification(
                account.getCustomer().getId(),
                NotificationType.WITHDRAWAL,
                "₹" + request.getAmount()
                        + " withdrawn from account "
                        + account.getAccountNumber()
                        + ". Available balance: ₹"
                        + newBalance,
                savedTransaction.getTransactionReference()
        );

        return toResponse(savedTransaction);
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    @Transactional
    public TransactionResponse transfer(
            String sourceAccountNumber,
            TransferRequest request) {

        Account sourceAccount =
                getAccount(sourceAccountNumber);

        Account destinationAccount =
                getAccount(
                        request.getDestinationAccountNumber()
                );

        if (sourceAccount.getAccountNumber()
                .equals(destinationAccount.getAccountNumber())) {

            throw new AccountOperationException(
                    "Source and destination accounts cannot be the same"
            );
        }

        validateActiveAccount(sourceAccount);
        validateActiveAccount(destinationAccount);
        validateAmount(request.getAmount());

        if (sourceAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new AccountOperationException(
                    "Insufficient balance in source account"
            );
        }

        String transferReference =
                generateTransferReference();

        BigDecimal sourceNewBalance =
                sourceAccount.getBalance()
                        .subtract(request.getAmount());

        sourceAccount.setBalance(sourceNewBalance);

        accountRepository.save(sourceAccount);

        BigDecimal destinationNewBalance =
                destinationAccount.getBalance()
                        .add(request.getAmount());

        destinationAccount.setBalance(
                destinationNewBalance
        );

        accountRepository.save(destinationAccount);

        Transaction debitTransaction =
                new Transaction();

        debitTransaction.setTransactionReference(
                generateTransactionReference()
        );

        debitTransaction.setAccount(
                sourceAccount
        );

        debitTransaction.setTransactionType(
                TransactionType.TRANSFER_DEBIT
        );

        debitTransaction.setAmount(
                request.getAmount()
        );

        debitTransaction.setBalanceAfter(
                sourceNewBalance
        );

        debitTransaction.setTransferReference(
                transferReference
        );

        debitTransaction.setDescription(
                request.getDescription()
        );

        debitTransaction.setCreatedAt(
                LocalDateTime.now()
        );

        Transaction savedDebitTransaction =
                transactionRepository.save(
                        debitTransaction
                );

        Transaction creditTransaction =
                new Transaction();

        creditTransaction.setTransactionReference(
                generateTransactionReference()
        );

        creditTransaction.setAccount(
                destinationAccount
        );

        creditTransaction.setTransactionType(
                TransactionType.TRANSFER_CREDIT
        );

        creditTransaction.setAmount(
                request.getAmount()
        );

        creditTransaction.setBalanceAfter(
                destinationNewBalance
        );

        creditTransaction.setTransferReference(
                transferReference
        );

        creditTransaction.setDescription(
                request.getDescription()
        );

        creditTransaction.setCreatedAt(
                LocalDateTime.now()
        );

        Transaction savedCreditTransaction =
                transactionRepository.save(
                        creditTransaction
                );

        // Source notification
        notificationService.createNotification(
                sourceAccount.getCustomer().getId(),
                NotificationType.TRANSFER,
                "₹" + request.getAmount()
                        + " transferred from account "
                        + sourceAccount.getAccountNumber()
                        + " to account "
                        + destinationAccount.getAccountNumber()
                        + ". Available balance: ₹"
                        + sourceNewBalance,
                savedDebitTransaction
                        .getTransactionReference()
        );

        // Destination notification
        notificationService.createNotification(
                destinationAccount.getCustomer().getId(),
                NotificationType.TRANSFER,
                "₹" + request.getAmount()
                        + " received in account "
                        + destinationAccount.getAccountNumber()
                        + " from account "
                        + sourceAccount.getAccountNumber()
                        + ". Available balance: ₹"
                        + destinationNewBalance,
                savedCreditTransaction
                        .getTransactionReference()
        );

        return toResponse(
                savedDebitTransaction
        );
    }

    // =========================================================
    // GET ALL TRANSACTIONS
    // =========================================================

    public List<TransactionResponse>
    getTransactionHistory(
            String accountNumber) {

        Account account =
                getAccount(accountNumber);

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(
                        account.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET TRANSACTIONS BY TYPE
    // =========================================================

    public List<TransactionResponse>
    getTransactionsByType(
            String accountNumber,
            TransactionType transactionType) {

        Account account =
                getAccount(accountNumber);

        if (transactionType == null) {

            throw new AccountOperationException(
                    "Transaction type is required"
            );
        }

        return transactionRepository
                .findByAccountIdAndTransactionTypeOrderByCreatedAtDesc(
                        account.getId(),
                        transactionType
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET TRANSACTION BY REFERENCE
    // =========================================================

    public TransactionResponse getTransactionByReference(
            String transactionReference) {

        Transaction transaction =
                transactionRepository
                        .findByTransactionReference(
                                transactionReference
                        )
                        .orElseThrow(() ->
                                new TransactionNotFoundException(
                                        "Transaction not found with reference: "
                                                + transactionReference
                                )
                        );

        return toResponse(transaction);
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private TransactionResponse toResponse(
            Transaction transaction) {

        TransactionResponse response =
                new TransactionResponse();

        response.setId(
                transaction.getId()
        );

        response.setTransactionReference(
                transaction.getTransactionReference()
        );

        response.setAccountNumber(
                transaction.getAccount()
                        .getAccountNumber()
        );

        response.setTransactionType(
                transaction.getTransactionType()
        );

        response.setAmount(
                transaction.getAmount()
        );

        response.setBalanceAfter(
                transaction.getBalanceAfter()
        );

        response.setTransferReference(
                transaction.getTransferReference()
        );

        response.setDescription(
                transaction.getDescription()
        );

        response.setCreatedAt(
                transaction.getCreatedAt()
        );

        return response;
    }

    // =========================================================
    // FIND ACCOUNT
    // =========================================================

    private Account getAccount(
            String accountNumber) {

        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with account number: "
                                        + accountNumber
                        )
                );
    }

    // =========================================================
    // VALIDATE ACCOUNT
    // =========================================================

    private void validateActiveAccount(
            Account account) {

        if (account.getStatus()
                != AccountStatus.ACTIVE) {

            throw new AccountOperationException(
                    "Account "
                            + account.getAccountNumber()
                            + " is not active"
            );
        }
    }

    // =========================================================
    // VALIDATE AMOUNT
    // =========================================================

    private void validateAmount(
            BigDecimal amount) {

        if (amount == null
                || amount.compareTo(
                BigDecimal.ZERO
        ) <= 0) {

            throw new AccountOperationException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    // =========================================================
    // TRANSACTION REFERENCE
    // =========================================================

    private String generateTransactionReference() {

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyyMMddHHmmss"
                                )
                        );

        return "TXN-" + timestamp + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    // =========================================================
    // TRANSFER REFERENCE
    // =========================================================

    private String generateTransferReference() {

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyyMMddHHmmss"
                                )
                        );

        return "TRF-" + timestamp + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}