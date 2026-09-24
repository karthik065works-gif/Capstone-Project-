package com.icici.repository;

import com.icici.model.Transaction;
import com.icici.model.TransactionType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find transaction using transaction reference
    Optional<Transaction> findByTransactionReference(
            String transactionReference
    );

    // Get all transactions for an account
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(
            Long accountId
    );

    // Get all transactions of a particular type for an account
    List<Transaction> findByAccountIdAndTransactionTypeOrderByCreatedAtDesc(
            Long accountId,
            TransactionType transactionType
    );

    // Get all transactions belonging to a transfer
    List<Transaction> findByTransferReference(
            String transferReference
    );

    // Check whether transaction reference already exists
    boolean existsByTransactionReference(
            String transactionReference
    );
}