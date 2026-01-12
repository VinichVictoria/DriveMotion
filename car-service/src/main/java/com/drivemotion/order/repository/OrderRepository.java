package com.drivemotion.order.repository;

import com.drivemotion.order.model.RentalOrder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderRepository {

    @PersistenceContext(unitName = "CarPU")
    private EntityManager em;

    @Transactional
    public void create(RentalOrder order) {
        em.persist(order);
    }


    public RentalOrder findById(UUID id) {
        return em.find(RentalOrder.class, id);
    }


    @Transactional
    public void update(RentalOrder order) {
        em.merge(order);
    }

    public BigDecimal getTotalRevenue() {

        return (BigDecimal) em.createQuery("SELECT SUM(c.pricePerDay) FROM RentalOrder o JOIN o.car c WHERE o.status = 'CONFIRMED'")
                .getSingleResult();
    }

    public List<RentalOrder> findAll() {
        return em.createQuery("SELECT o FROM RentalOrder o", RentalOrder.class).getResultList();
    }
}