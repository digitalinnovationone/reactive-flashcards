package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse(@JsonProperty("status")
                              Integer status,
                              @JsonProperty("timestamp")
                              OffsetDateTime timestamp,
                              @JsonProperty("errorDescription")
                              String errorDescription,
                              @JsonProperty("fields")
                              List<ErrorFieldResponse> fields) {

    @Builder(toBuilder = true)
    public ProblemResponse{ }

}
