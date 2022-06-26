package br.com.dio.reactiveflashcards.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record CardDTO(@JsonProperty("ask")
                      String ask,
                      @JsonProperty("ask")
                      String answer) {

    @Builder(toBuilder = true)
    public CardDTO { }
}
