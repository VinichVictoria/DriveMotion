package com.drivemotion.admin.resource;

import com.drivemotion.order.repository.OrderRepository;
import com.drivemotion.car.repository.CarRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/admin/reports")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("MANAGER")
public class ReportResource {

    @Inject
    private OrderRepository orderRepository;
    @Inject
    private CarRepository carRepository;

    @GET
    @Path("/summary")
    public Response getFleetSummary() {
        long totalOrders = orderRepository.findAll().size();
        long activeCars = carRepository.findAll().stream()
                .filter(c -> "RENTED".equals(c.getStatus())).count();


        Map<String, Object> report = Map.of(
                "total_orders", totalOrders,
                "cars_currently_rented", activeCars,
                "fleet_size", carRepository.findAll().size()
        );

        return Response.ok(report).build();
    }
}