package com.drivemotion.order.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OrderRequest {
    private String carId;
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
}