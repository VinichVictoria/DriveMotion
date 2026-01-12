package com.drivemotion.order.resource;

import com.drivemotion.car.model.Car;
import com.drivemotion.car.repository.CarRepository;
import com.drivemotion.order.model.OrderRequest;
import com.drivemotion.order.model.RentalOrder;
import com.drivemotion.order.repository.OrderRepository;
import com.drivemotion.user.model.User;
import com.drivemotion.user.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    private OrderRepository orderRepository;
    @Inject
    private CarRepository carRepository;
    @Inject
    private UserRepository userRepository;

    @POST
    // @RolesAllowed("CLIENT") // Тимчасово відключено для тесту
    public Response createOrder(OrderRequest request) {
        Car car = carRepository.findById(UUID.fromString(request.getCarId()));
        if (car == null) return Response.status(Response.Status.NOT_FOUND).entity("Car not found").build();

        User user = userRepository.findById(UUID.fromString(request.getUserId()));
        if (user == null) return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();

        if (!"AVAILABLE".equals(car.getStatus())) {
            return Response.status(Response.Status.CONFLICT).entity("Car is not available").build();
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) days = 1;
        BigDecimal totalCost = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        RentalOrder order = new RentalOrder();
        order.setCar(car);
        order.setUser(user);
        order.setStartDate(request.getStartDate());
        order.setEndDate(request.getEndDate());
        order.setStatus("PENDING");

        orderRepository.create(order);

        return Response.status(Response.Status.CREATED)
                .entity("{\"orderId\":\"" + order.getId() + "\", \"totalPrice\":" + totalCost + ", \"status\":\"PENDING\"}")
                .build();
    }

    @POST
    @Path("/{id}/pay")
    // @RolesAllowed("CLIENT") // Тимчасово відключено
    public Response processPayment(@PathParam("id") String id) {
        RentalOrder order = orderRepository.findById(UUID.fromString(id));
        if (order == null) return Response.status(Response.Status.NOT_FOUND).build();

        order.setStatus("PAID");
        orderRepository.update(order);

        return Response.ok("{\"message\": \"Payment successful\", \"status\": \"PAID\"}").build();
    }

    @PUT
    @Path("/{id}/status")
    // @RolesAllowed("MANAGER") // Тимчасово відключено
    public Response updateStatus(@PathParam("id") String id, @QueryParam("status") String status) {
        RentalOrder order = orderRepository.findById(UUID.fromString(id));
        if (order == null) return Response.status(Response.Status.NOT_FOUND).build();

        if ("CONFIRMED".equals(status)) {
            order.getCar().setStatus("RENTED");
        } else if ("CANCELLED".equals(status)) {
            order.getCar().setStatus("AVAILABLE");
        }

        order.setStatus(status);
        orderRepository.update(order);
        return Response.ok(order).build();
    }

    @GET
    // @RolesAllowed({"MANAGER", "CLIENT"}) // Тимчасово відключено
    public Response getAllOrders() {
        return Response.ok(orderRepository.findAll()).build();
    }
}