package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record ErrorFieldResponse(@JsonProperty("name")
                                 @Schema(description = "nome do campo com erro", example = "name")
                                 String name,
                                 @JsonProperty("message")
                                 @Schema(description = "descrição do erro", example = "o nome deve ter no máximo 255 caractéres")
                                 String message) {

    @Builder(toBuilder = true)
    public ErrorFieldResponse{ }

}
