package de.infinity.jwt.dto;

import de.infinity.jwt.enumeration.AccountType;

public class CreateAccountDto {
    private AccountType accountType;

    public CreateAccountDto(AccountType accountType) {
        this.accountType = accountType;
    }

    public CreateAccountDto() {
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
