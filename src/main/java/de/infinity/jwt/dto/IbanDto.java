package de.infinity.jwt.dto;

import javax.validation.constraints.NotEmpty;

public class IbanDto {
    @NotEmpty(message = "iban should not be empty.")
    private String iban;

    public IbanDto(String iban) {
        this.iban = iban;
    }

    public IbanDto() {
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
