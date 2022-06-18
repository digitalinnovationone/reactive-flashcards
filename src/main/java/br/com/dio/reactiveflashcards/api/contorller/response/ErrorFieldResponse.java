package br.com.dio.reactiveflashcards.api.contorller.response;

import lombok.Builder;

public record ErrorFieldResponse(String name,
                                 String message) {

    @Builder(toBuilder = true)
    public ErrorFieldResponse{ }

}
