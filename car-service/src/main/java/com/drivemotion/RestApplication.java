package com.drivemotion;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath("/api")
@LoginConfig(authMethod = "MP-JWT")
@OpenAPIDefinition(info = @Info(
        title = "DriveMotion API",
        version = "1.0.0",
        description = "Система прокату автомобілів"
))
public class RestApplication extends Application {
}