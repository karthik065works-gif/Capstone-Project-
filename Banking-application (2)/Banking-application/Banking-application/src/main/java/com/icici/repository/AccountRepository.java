package com.icici.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icici.model.Account;
import com.icici.model.AccountType;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(
            String accountNumber
    );

    boolean existsByCustomerIdAndAccountType(
            Long customerId,
            AccountType accountType
    );

    boolean existsByCustomerIdAndAccountTypeAndIdNot(
            Long customerId,
            AccountType accountType,
            Long accountId
    );
}