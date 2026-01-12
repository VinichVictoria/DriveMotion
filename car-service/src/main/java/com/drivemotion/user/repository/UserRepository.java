package com.drivemotion.user.repository;

import com.drivemotion.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "CarPU")
    private EntityManager em;

    @Transactional
    public void create(User user) {
        em.persist(user);
    }


    public User findById(UUID id) {
        return em.find(User.class, id);
    }


    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}