package de.infinity.jwt.model;

import de.infinity.jwt.enumeration.AccountType;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "bankaccounts")
@NamedQueries({
        @NamedQuery(name = BankAccount.NQ_GET_BY_CUSTOMER_ID,
                query = " SELECT new de.infinity.jwt.dto.BankAccountDto(ba.id,ba.iban,ba.type,ba.balance,ba.active) FROM BankAccount ba " +
                        " WHERE ba.customer.id = :customerId "),
        @NamedQuery(name = BankAccount.NQ_GET_BY_ID,
                query = " SELECT new de.infinity.jwt.dto.BankAccountDto(ba.id,ba.iban,ba.type,ba.balance,ba.active) FROM BankAccount ba " +
                        " WHERE ba.customer.id = :customerId AND ba.id = :id"),
        @NamedQuery(name = BankAccount.NQ_GET_BY_IBAN,
                query = " SELECT ba FROM BankAccount ba " +
                        " WHERE ba.iban = :iban "),
        @NamedQuery(name = BankAccount.NQ_GET_BY_CUSTOMER_ID_AND_IBAN,
                query = " SELECT ba FROM BankAccount ba " +
                        " WHERE ba.customer.id = :customerId AND ba.iban = :iban"),
        @NamedQuery(name = BankAccount.NQ_GET_BY_CUSTOMER_ID_AND_ACCOUNT_TYPE,
                query = " SELECT new de.infinity.jwt.dto.BankAccountDto(ba.id,ba.iban,ba.type,ba.balance,ba.active) FROM BankAccount ba " +
                        " WHERE ba.customer.id = :customerId AND ba.type = :accountType")
})
public class BankAccount {

    public static final String NQ_GET_BY_CUSTOMER_ID = "BankAccount.getByCustomerId";
    public static final String NQ_GET_BY_ID = "BankAccount.getById";
    public static final String NQ_GET_BY_IBAN = "BankAccount.getByIban";
    public static final String NQ_GET_BY_CUSTOMER_ID_AND_IBAN = "BankAccount.getByCustomerIdAndIban";
    public static final String NQ_GET_BY_CUSTOMER_ID_AND_ACCOUNT_TYPE = "BankAccount.getByCustomerIdAndAccountType";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_id_generator")
    @SequenceGenerator(name = "bank_id_generator", sequenceName = "bank_id_seq", allocationSize = 1)
    private Long id;

    @Column(unique=true)
    private String iban;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @PositiveOrZero
    private Double balance;

    private Boolean active;

    public BankAccount(Long id, String iban, AccountType type, Double balance, Boolean active, Customer customer) {
        this.id = id;
        this.iban = iban;
        this.type = type;
        this.balance = balance;
        this.active = active;
        this.customer = customer;
    }

    public BankAccount(AccountType type, Customer customer) {
        this.iban = String.format("DE%s", RandomStringUtils.randomNumeric(20));
        this.type = type;
        this.customer = customer;
        this.balance = 0.0;
        this.active = Boolean.TRUE;
    }

    public BankAccount() {
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
