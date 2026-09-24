package com.icici.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dto.AccountRequest;
import com.icici.dto.AccountResponse;
import com.icici.exception.AccountNotFoundException;
import com.icici.exception.AccountOperationException;
import com.icici.exception.CustomerNotFoundException;
import com.icici.model.Account;
import com.icici.model.AccountStatus;
import com.icici.model.Customer;
import com.icici.model.CustomerStatus;
import com.icici.repository.AccountRepository;
import com.icici.repository.CustomerRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    // =========================================================
    // CREATE ACCOUNT
    // =========================================================

    @Transactional
    public AccountResponse createAccount(
            Long customerId,
            AccountRequest request) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id: "
                                        + customerId
                        )
                );

        // Customer must be active
        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new AccountOperationException(
                    "Cannot create account for inactive customer"
            );
        }

        // Customer can have only one account of each type
        if (accountRepository
                .existsByCustomerIdAndAccountType(
                        customerId,
                        request.getAccountType()
                )) {

            throw new AccountOperationException(
                    "Customer already has an account of type: "
                            + request.getAccountType()
            );
        }

        // Initial balance validation
        if (request.getInitialBalance() == null
                || request.getInitialBalance()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new AccountOperationException(
                    "Initial balance cannot be negative"
            );
        }

        Account account = new Account();

        account.setAccountNumber(
                generateAccountNumber()
        );

        account.setCustomer(customer);

        account.setAccountType(
                request.getAccountType()
        );

        account.setBalance(
                request.getInitialBalance()
        );

        account.setStatus(
                AccountStatus.ACTIVE
        );

        account.setCreatedAt(
                LocalDateTime.now()
        );

        Account savedAccount =
                accountRepository.save(account);

        return toResponse(savedAccount);
    }

    // =========================================================
    // GET ACCOUNT
    // =========================================================

    public AccountResponse getAccountByNumber(
            String accountNumber) {

        Account account =
                findAccount(accountNumber);

        return toResponse(account);
    }

    // =========================================================
    // UPDATE ACCOUNT
    // =========================================================

    @Transactional
    public AccountResponse updateAccount(
            String accountNumber,
            AccountRequest request) {

        Account account =
                findAccount(accountNumber);

        // Closed account cannot be updated
        if (account.getStatus()
                == AccountStatus.CLOSED) {

            throw new AccountOperationException(
                    "Closed account cannot be updated"
            );
        }

        // Account type must be provided
        if (request.getAccountType() == null) {

            throw new AccountOperationException(
                    "Account type is required"
            );
        }

        // Balance must not be negative
        if (request.getInitialBalance() == null
                || request.getInitialBalance()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new AccountOperationException(
                    "Balance cannot be negative"
            );
        }

        // Check duplicate account type
        // Exclude the current account itself
        if (accountRepository
                .existsByCustomerIdAndAccountTypeAndIdNot(
                        account.getCustomer().getId(),
                        request.getAccountType(),
                        account.getId()
                )) {

            throw new AccountOperationException(
                    "Customer already has another account of type: "
                            + request.getAccountType()
            );
        }

        account.setAccountType(
                request.getAccountType()
        );

        account.setBalance(
                request.getInitialBalance()
        );

        Account updatedAccount =
                accountRepository.save(account);

        return toResponse(updatedAccount);
    }

    // =========================================================
    // CLOSE ACCOUNT
    // =========================================================

    @Transactional
    public AccountResponse closeAccount(
            String accountNumber) {

        Account account =
                findAccount(accountNumber);

        if (account.getStatus()
                == AccountStatus.CLOSED) {

            throw new AccountOperationException(
                    "Account is already closed"
            );
        }

        // Account must have zero balance
        if (account.getBalance()
                .compareTo(BigDecimal.ZERO) != 0) {

            throw new AccountOperationException(
                    "Account balance must be zero before closing"
            );
        }

        account.setStatus(
                AccountStatus.CLOSED
        );

        Account closedAccount =
                accountRepository.save(account);

        return toResponse(closedAccount);
    }

    // =========================================================
    // DEACTIVATE ACCOUNT
    // =========================================================

    @Transactional
    public AccountResponse deactivateAccount(
            String accountNumber) {

        Account account =
                findAccount(accountNumber);

        if (account.getStatus()
                == AccountStatus.CLOSED) {

            throw new AccountOperationException(
                    "Closed account cannot be deactivated"
            );
        }

        account.setStatus(
                AccountStatus.INACTIVE
        );

        Account updatedAccount =
                accountRepository.save(account);

        return toResponse(updatedAccount);
    }

    // =========================================================
    // ACTIVATE ACCOUNT
    // =========================================================

    @Transactional
    public AccountResponse activateAccount(
            String accountNumber) {

        Account account =
                findAccount(accountNumber);

        if (account.getStatus()
                == AccountStatus.CLOSED) {

            throw new AccountOperationException(
                    "Closed account cannot be activated"
            );
        }

        // Customer must also be active
        if (account.getCustomer().getStatus()
                != CustomerStatus.ACTIVE) {

            throw new AccountOperationException(
                    "Account cannot be activated because customer is inactive"
            );
        }

        account.setStatus(
                AccountStatus.ACTIVE
        );

        Account updatedAccount =
                accountRepository.save(account);

        return toResponse(updatedAccount);
    }

    // =========================================================
    // FIND ACCOUNT
    // =========================================================

    private Account findAccount(
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
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private AccountResponse toResponse(
            Account account) {

        AccountResponse response =
                new AccountResponse();

        response.setId(
                account.getId()
        );

        response.setAccountNumber(
                account.getAccountNumber()
        );

        response.setCustomerId(
                account.getCustomer().getId()
        );

        response.setAccountType(
                account.getAccountType()
        );

        response.setBalance(
                account.getBalance()
        );

        response.setStatus(
                account.getStatus()
        );

        response.setCreatedAt(
                account.getCreatedAt()
        );

        return response;
    }

    // =========================================================
    // ACCOUNT NUMBER GENERATION
    // =========================================================

    private String generateAccountNumber() {

        long count =
                accountRepository.count();

        return String.valueOf(
                1000000001L + count
        );
    }
}