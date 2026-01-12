package com.drivemotion.user.resource;

import com.drivemotion.user.model.LoginRequest;
import com.drivemotion.user.model.User;
import com.drivemotion.user.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;
import java.util.Base64;
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;


    @POST
    @Path("/register")
    public Response registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Email already exists").build();
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("CLIENT");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.create(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }


    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();


            if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {

                String mySecret = "z39v8x4N2m1L5k8P0z39v8x4N2m1L5k8P0z39v8x4N2m1L5k8P0z39v8x4N2m1";

                String token = io.smallrye.jwt.build.Jwt.issuer("https://drivemotion.com")
                        .upn(user.getEmail())
                        .groups(user.getRole())
                        .claim("name", user.getFullName())
                        .signWithSecret(mySecret); // ТУТ МАЄ БУТИ ПРОСТО STRING

                return Response.ok("{\"token\": \"" + token + "\"}").build();
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid email or password").build();
    }

    @GET
    public Response getAllUsers() {
        return Response.ok(userRepository.findAll()).build();
    }
}