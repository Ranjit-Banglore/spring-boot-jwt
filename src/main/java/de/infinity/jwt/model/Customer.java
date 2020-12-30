package de.infinity.jwt.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
@NamedQueries({
        @NamedQuery(name = Customer.NQ_GET_BY_ID,
                query = " SELECT c FROM Customer c WHERE c.id = :customerId"
        )
})
public class Customer {

    public static final String NQ_GET_BY_ID = "Customer.getById";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_generator")
    @SequenceGenerator(name = "customer_id_generator", sequenceName = "customer_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    private List<BankAccount> bankAccounts;

    public Customer(Long id, String name, String email, String phone, List<BankAccount> bankAccounts) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bankAccounts = bankAccounts;
    }

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
