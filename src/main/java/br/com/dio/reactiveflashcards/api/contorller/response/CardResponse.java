package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public record CardResponse(@JsonProperty("front")
                           @Schema(description = "pergunta do card", example = "blue")
                           String front,
                           @JsonProperty("back")
                           @Schema(description = "resposta do card", example = "azul")
                           String back) {

    @Builder(toBuilder = true)
    public CardResponse {}

}
