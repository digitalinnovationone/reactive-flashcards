package br.com.dio.reactiveflashcards.api.contorller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CardRequest(@JsonProperty("front")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          String front,
                          @JsonProperty("back")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          String back) {

    @Builder(toBuilder = true)
    public CardRequest {}

}
