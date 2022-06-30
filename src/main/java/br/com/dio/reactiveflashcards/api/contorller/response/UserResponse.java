package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record UserResponse(@JsonProperty("id")
                           @Schema(description = "identificador do usuário", format = "UUID", example = "62bdec5e5a8f2441c4d27460")
                           String id,
                           @JsonProperty("name")
                           @Schema(description = "nome do usuário", example = "João")
                           String name,
                           @JsonProperty("email")
                           @Schema(description = "email do usuário", example = "joao@joao.com.br")
                           String email) {

    @Builder(toBuilder = true)
    public UserResponse { }

}
