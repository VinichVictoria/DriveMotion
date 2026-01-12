package com.drivemotion.car.repository;

import com.drivemotion.car.model.Car;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CarRepository {

    @PersistenceContext(unitName = "CarPU")
    private EntityManager em;


    @Transactional
    public void create(Car car) {
        em.persist(car);
    }


    public Car findById(UUID id) {
        return em.find(Car.class, id);
    }


    public List<Car> findAll() {
        return em.createQuery("SELECT c FROM Car c", Car.class).getResultList();
    }


    public List<Car> findByStatus(String status) {
        return em.createQuery("SELECT c FROM Car c WHERE c.status = :status", Car.class)
                .setParameter("status", status)
                .getResultList();
    }
}