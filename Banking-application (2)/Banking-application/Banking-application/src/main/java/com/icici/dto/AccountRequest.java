package com.icici.dto;

import java.math.BigDecimal;

import com.icici.model.AccountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class AccountRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Initial balance is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Initial balance cannot be negative"
    )
    private BigDecimal initialBalance;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public AccountRequest() {
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}