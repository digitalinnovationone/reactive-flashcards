package br.com.dio.reactiveflashcards.core;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Reactive Flashcards", description = "API reativa de estudo de flashcards"),
        servers = {
                @Server(url = "http://localhost:8080/reactive-flashcards", description = "local")
        }
)
public class OpenApiConfig {
}
