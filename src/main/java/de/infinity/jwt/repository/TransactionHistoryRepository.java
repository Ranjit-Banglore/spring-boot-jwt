package de.infinity.jwt.repository;

import de.infinity.jwt.model.TransactionHistory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TransactionHistoryRepository {
    @PersistenceContext
    EntityManager entityManager;

    public List<TransactionHistory> getTransactionHistory(String iban) {
        return entityManager.createNamedQuery(TransactionHistory.NQ_GET_BY_IBAN, TransactionHistory.class)
                .setParameter("iban", iban)
                .getResultList();
    }
}
