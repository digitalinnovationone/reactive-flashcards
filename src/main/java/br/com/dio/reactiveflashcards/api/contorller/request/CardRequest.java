package br.com.dio.reactiveflashcards.api.contorller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CardRequest(@JsonProperty("front")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "pergunta do card", example = "blue")
                          String front,
                          @JsonProperty("back")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "resposta do card", example = "azul")
                          String back) {

    @Builder(toBuilder = true)
    public CardRequest {}

}
