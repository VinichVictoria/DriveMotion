package com.drivemotion.order.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private RentalOrder order;

    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String status; // "COMPLETED", "FAILED"
}