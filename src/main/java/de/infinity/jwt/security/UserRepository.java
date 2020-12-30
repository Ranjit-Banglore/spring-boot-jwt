package de.infinity.jwt.security;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public User findByUserName(String userName) {
        return entityManager.createNamedQuery(User.NQ_GET_BY_NAME,User.class)
                    .setParameter("userName", userName)
                    .getSingleResult();
    }
}
