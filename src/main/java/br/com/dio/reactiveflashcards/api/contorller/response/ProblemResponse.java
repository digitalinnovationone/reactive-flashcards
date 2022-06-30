package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse(@JsonProperty("status")
                              @Schema(description = "http status retornado", example = "400")
                              Integer status,
                              @JsonProperty("timestamp")
                              @Schema(description = "momento em que o erro aconteceu", format = "datetime", example = "2022-07-30T12:30:00Z")
                              OffsetDateTime timestamp,
                              @JsonProperty("errorDescription")
                              @Schema(description = "descrição do erro", example = "sua requisição tem informa'ões inválidas")
                              String errorDescription,
                              @JsonProperty("fields")
                              @Schema(description = "caso a requisição tenha parâmetros inválidos aqui serão informados os erros referentes aos mesmos")
                              List<ErrorFieldResponse> fields) {

    @Builder(toBuilder = true)
    public ProblemResponse{ }

}
