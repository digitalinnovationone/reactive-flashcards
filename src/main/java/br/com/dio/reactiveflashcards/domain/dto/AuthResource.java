package br.com.dio.reactiveflashcards.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record AuthResource(@JsonProperty("token")
                           String token,
                           @JsonProperty("expiresIn")
                           Long expiresIn) {

    @Builder(toBuilder = true)
    public AuthResource { }
}
