package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ProblemResponse(Integer status,
                              OffsetDateTime timestamp,
                              String errorDescription,
                              List<ErrorFieldResponse> fields) {

    @Builder(toBuilder = true)
    public ProblemResponse{ }

}
