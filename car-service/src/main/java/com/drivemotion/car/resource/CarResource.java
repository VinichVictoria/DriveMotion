package com.drivemotion.car.resource;

import com.drivemotion.car.model.Car;
import com.drivemotion.car.repository.CarRepository;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CarResource {

    @Inject
    private CarRepository carRepository;

    @GET
    @PermitAll
    public List<Car> getCars(@QueryParam("status") String status) {
        if (status != null && !status.isEmpty()) {
            return carRepository.findByStatus(status);
        }
        return carRepository.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCar(Car car) {
        carRepository.create(car);
        return Response.status(Response.Status.CREATED).entity(car).build();
    }
}