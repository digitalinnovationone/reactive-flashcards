package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record ErrorFieldResponse(@JsonProperty("name")
                                 String name,
                                 @JsonProperty("message")
                                 String message) {

    @Builder(toBuilder = true)
    public ErrorFieldResponse{ }

}
