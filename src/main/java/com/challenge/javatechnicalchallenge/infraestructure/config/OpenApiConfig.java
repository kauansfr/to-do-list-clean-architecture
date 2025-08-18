package com.challenge.javatechnicalchallenge.infraestructure.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "To-Do List API",
                version = "v1",
                description = "API RESTful para gerenciamento de tarefas (To-Do List)",
                contact = @Contact(name = "Kauan", email = "20kauanfranca03@gmail.com")
        ),
        servers = {
            @Server(
                    url = "{scheme}://{host}:{port}",
                    description = "Servidor parametrizado",
                    variables = {
                        @ServerVariable(
                                name = "scheme",
                                description = "Protocolo",
                                defaultValue = "http",
                                allowableValues = {"http", "https"}
                        ),
                        @ServerVariable(
                                name = "host",
                                description = "Host ou IP",
                                defaultValue = ""
                        ),
                        @ServerVariable(
                                name = "port",
                                description = "Porta",
                                defaultValue = "8080"
                        )
                    }
            )
        }
)
public class OpenApiConfig {
}
