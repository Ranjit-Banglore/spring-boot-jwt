package de.infinity.jwt.model;

import de.infinity.jwt.enumeration.TransactionType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction_history")
@NamedQueries({
        @NamedQuery(name = TransactionHistory.NQ_GET_BY_IBAN,
                query = " SELECT tr FROM TransactionHistory tr " +
                        " WHERE tr.iban = :iban ")
})
public class TransactionHistory {

    public static final String NQ_GET_BY_IBAN = "TransactionHistory.getByIban";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_generator")
    @SequenceGenerator(name = "transaction_id_generator", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;

    private String iban;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Date date = new Date();
    private Double netBalance;

    public TransactionHistory(String iban, Double amount, TransactionType transactionType, Double netBalance) {
        this.id = id;
        this.iban = iban;
        this.amount = amount;
        this.type = transactionType;
        this.netBalance = netBalance;
    }

    public TransactionHistory() {
    }

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(Double netBalance) {
        this.netBalance = netBalance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
