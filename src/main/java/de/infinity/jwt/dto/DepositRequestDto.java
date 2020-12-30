package de.infinity.jwt.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class DepositRequestDto {
    @NotEmpty(message = "iban should not be empty.")
    private String iban;

    @Positive(message = "Amount should be greater than zero.")
    private Double amount;

    public DepositRequestDto(String iban, Double amount) {
        this.iban = iban;
        this.amount = amount;
    }

    public DepositRequestDto() {
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
