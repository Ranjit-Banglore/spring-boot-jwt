package de.infinity.jwt.repository;

import de.infinity.jwt.dto.BankAccountDto;
import de.infinity.jwt.enumeration.AccountType;
import de.infinity.jwt.model.BankAccount;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BankAccountRepository {
    //https://stackoverflow.com/questions/31335211/autowired-vs-persistencecontext-for-entitymanager-bean
    @PersistenceContext
    private EntityManager entityManager;

    public List<BankAccountDto> getBankAccountByCustomerId(Long customerId) {
        return entityManager.createNamedQuery(BankAccount.NQ_GET_BY_CUSTOMER_ID, BankAccountDto.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public BankAccountDto getBankAccountById( Long customerId, Long id) {
        return entityManager.createNamedQuery(BankAccount.NQ_GET_BY_ID, BankAccountDto.class)
                .setParameter("customerId", customerId)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<BankAccountDto> getBankAccountByCustomerIdAndAccountType(Long customerId, AccountType filter) {
        return entityManager.createNamedQuery(BankAccount.NQ_GET_BY_CUSTOMER_ID_AND_ACCOUNT_TYPE, BankAccountDto.class)
                .setParameter("customerId", customerId)
                .setParameter("accountType", filter)
                .getResultList();
    }

    public BankAccount getBankAccountByIban(String iban) {
        return entityManager.createNamedQuery(BankAccount.NQ_GET_BY_IBAN, BankAccount.class)
                .setParameter("iban", iban)
                .getSingleResult();
    }

    public BankAccount getBankAccountByCustomerIdAndIban(Long customerId, String iban){
        return entityManager.createNamedQuery(BankAccount.NQ_GET_BY_CUSTOMER_ID_AND_IBAN, BankAccount.class)
                .setParameter("customerId", customerId)
                .setParameter("iban", iban)
                .getSingleResult();
    }

    public Double getBalanceByCustomerIdAndIban(Long customerId, String iban){
        return entityManager.createNamedQuery(BankAccount.NQ_GET_BY_CUSTOMER_ID_AND_IBAN, BankAccount.class)
                .setParameter("customerId", customerId)
                .setParameter("iban", iban)
                .getSingleResult()
                .getBalance();
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    public <T> T merge(T entity) {
        return entityManager.merge(entity);
    }

}
