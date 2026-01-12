package com.drivemotion.car.repository;

import com.drivemotion.car.model.MaintenanceLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MaintenanceRepository {
    @PersistenceContext(unitName = "CarPU")
    private EntityManager em;

    @Transactional
    public void save(MaintenanceLog log) {
        em.persist(log);
    }

    public List<MaintenanceLog> findByCarId(UUID carId) {
        return em.createQuery("SELECT m FROM MaintenanceLog m WHERE m.car.id = :carId", MaintenanceLog.class)
                .setParameter("carId", carId)
                .getResultList();
    }
}