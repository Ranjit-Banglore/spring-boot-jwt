package de.infinity.jwt.dto;

import de.infinity.jwt.enumeration.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BankAccountDto extends RepresentationModel<BankAccountDto> {
    private Long id;
    private String iban;
    private AccountType type;
    private Double balance;
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BankAccountDto(Long id, String iban, AccountType type, Double balance, Boolean active) {
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.balance = balance;
        this.active = active;
    }

    public BankAccountDto(){}
}
