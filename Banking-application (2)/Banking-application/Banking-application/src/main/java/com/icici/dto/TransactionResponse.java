package com.icici.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.icici.model.TransactionType;

public class TransactionResponse {

    private Long id;

    private String transactionReference;

    private String accountNumber;

    private TransactionType transactionType;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private String transferReference;

    private String description;

    private LocalDateTime createdAt;

    public TransactionResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(
            String transactionReference) {

        this.transactionReference = transactionReference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(
            String accountNumber) {

        this.accountNumber = accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(
            TransactionType transactionType) {

        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(
            BigDecimal balanceAfter) {

        this.balanceAfter = balanceAfter;
    }

    public String getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(
            String transferReference) {

        this.transferReference = transferReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description) {

        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }
}