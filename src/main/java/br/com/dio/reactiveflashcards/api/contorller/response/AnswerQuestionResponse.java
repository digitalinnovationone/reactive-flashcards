package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;

public record AnswerQuestionResponse(@JsonProperty("asked")
                                     @Schema(description = "pergunta feita", example = "blue")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     @Schema(description = "momento em que a pergunta foi gerada", format = "datetime", example = "2022-07-30T12:30:00Z")
                                     OffsetDateTime askedIn,
                                     @JsonProperty("answered")
                                     @Schema(description = "resposta fornecida", example = "azul")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     @Schema(description = "momento em que a pergunta foi respondida", format = "datetime", example = "2022-07-30T12:30:00Z")
                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     @Schema(description = "resposta esperada", example = "azul")
                                     String expected) {

    @Builder(toBuilder = true)
    public AnswerQuestionResponse { }
}
