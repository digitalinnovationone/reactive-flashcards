package br.com.dio.reactiveflashcards.api.contorller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public record DeckRequest(@JsonProperty("name")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "nome do deck", example = "estudo de inglês")
                          String name,
                          @JsonProperty("description")
                          @NotBlank
                          @Size(min = 1, max = 255)
                          @Schema(description = "descrição do deck", example = "deck de estudo de inglês para iniciantes")
                          String description,
                          @Valid
                          @Size(min = 3)
                          @NotNull
                          @JsonProperty("cards")
                          @Schema(description = "cards que compõe o deck")
                          Set<CardRequest> cards) {

    @Builder(toBuilder = true)
    public DeckRequest { }

}
