package de.infinity.jwt.repository;

import de.infinity.jwt.model.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Customer getCustomerById(Long customerId){
        return entityManager.createNamedQuery(Customer.NQ_GET_BY_ID, Customer.class)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }
}
