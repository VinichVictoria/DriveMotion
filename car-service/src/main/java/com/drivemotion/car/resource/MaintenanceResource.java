package com.drivemotion.car.resource;

import com.drivemotion.car.model.Car;
import com.drivemotion.car.model.MaintenanceLog;
import com.drivemotion.car.repository.CarRepository;
import com.drivemotion.car.repository.MaintenanceRepository;
import jakarta.annotation.security.RolesAllowed; // Додано для контролю доступу
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.UUID;

@Path("/maintenance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaintenanceResource {

    @Inject
    private MaintenanceRepository maintenanceRepository;
    @Inject
    private CarRepository carRepository;

    @POST
    @Path("/log")
    @RolesAllowed("MECHANIC")
    public Response addLog(@QueryParam("carId") String carId, String description) {
        Car car = carRepository.findById(UUID.fromString(carId));
        if (car == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Car not found").build();
        }

        MaintenanceLog log = new MaintenanceLog();
        log.setCar(car);
        log.setDescription(description);
        log.setDate(LocalDate.now());

        maintenanceRepository.save(log);


        car.setStatus("UNDER_MAINTENANCE");


        return Response.status(Response.Status.CREATED).entity(log).build();
    }

    @GET
    @Path("/car/{carId}")
    @RolesAllowed({"MECHANIC", "MANAGER"})
    public Response getHistory(@PathParam("carId") String carId) {
        UUID uuid = UUID.fromString(carId);
        return Response.ok(maintenanceRepository.findByCarId(uuid)).build();
    }
}