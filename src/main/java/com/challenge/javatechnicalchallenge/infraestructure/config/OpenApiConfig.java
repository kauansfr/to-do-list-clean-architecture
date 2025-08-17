package com.challenge.javatechnicalchallenge.infraestructure.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "To-Do List API",
                version = "v1",
                description = "API RESTful para gerenciamento de tarefas (To-Do List)",
                contact = @Contact(name = "Kauan", email = "20kauanfranca03@gmail.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local" )
        }
)
public class OpenApiConfig {
}
