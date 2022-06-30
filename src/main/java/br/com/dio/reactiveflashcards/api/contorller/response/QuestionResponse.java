package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;

public record QuestionResponse(@JsonProperty("id")
                               @Schema(description = "identificador do estudo", format = "UUID", example = "62bdec5e5a8f2441c4d27460")
                               String id,
                               @JsonProperty("asked")
                               @Schema(description = "perguta atual do estudo", example = "azul")
                               String asked,
                               @JsonProperty("askedIn")
                               @Schema(description = "quando a pergunta foi gerada", format = "datetime", example = "2022-07-30T12:30:00Z")
                               OffsetDateTime askedIn) {

    @Builder(toBuilder = true)
    public QuestionResponse { }
}
