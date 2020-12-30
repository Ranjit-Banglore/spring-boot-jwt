package de.infinity.jwt.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class TransferDto {
    @NotEmpty(message = "iban should not be empty.")
    private String fromIban;
    @NotEmpty(message = "iban should not be empty.")
    private String toIban;
    @Positive(message = "Amount should be greater than zero")
    private Double amount;

    public TransferDto(String fromIban, String toIban, Double amount) {
        this.fromIban = fromIban;
        this.toIban = toIban;
        this.amount = amount;
    }

    public TransferDto() {
    }

    public String getFromIban() {
        return fromIban;
    }

    public void setFromIban(String fromIban) {
        this.fromIban = fromIban;
    }

    public String getToIban() {
        return toIban;
    }

    public void setToIban(String toIban) {
        this.toIban = toIban;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
