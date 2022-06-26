package br.com.dio.reactiveflashcards.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CardDTO(@JsonProperty("ask")
                      String ask,
                      @JsonProperty("ask")
                      String answer) {

}
