package br.com.dio.reactiveflashcards.api.contorller.documentation;

import br.com.dio.reactiveflashcards.api.contorller.request.DeckRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.DeckResponse;
import br.com.dio.reactiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Deck", description = "Endpoints para manipulação de decks")
public interface DeckControllerDoc {

    @Operation(summary = "Endpoint para criar um novo deck")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar o deck criado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeckResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<DeckResponse> save(@Valid @RequestBody DeckRequest request);

    @Operation(summary = "Endpoint para buscar decks de uma api terceira")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "foram incluidos na base novos decks")
    })
    @PostMapping(value = "sync")
    Mono<Void> sync();

    @Operation(summary = "Endpoint para buscar um deck pelo seu identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o deck correspondete ao identificador",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeckResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<DeckResponse> findById(@Parameter(description = "identificador do deck", example = "62bdec5e5a8f2441c4d27460") @PathVariable
                                @Valid @MongoId(message = "{deckController.id}") String id);


    @Operation(summary = "Endpoint para buscar todos os decks")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar os decks cadastrados",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DeckResponse.class)))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Flux<DeckResponse> findAll();


    @Operation(summary = "Endpoint para atualizar um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "o deck foi atualizado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeckResponse.class))})
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<DeckResponse> update(@Parameter(description = "identificador do deck", example = "62bdec5e5a8f2441c4d27460") @PathVariable
                              @Valid @MongoId(message = "{deckController.id}") String id,
                              @Valid @RequestBody DeckRequest request);


    @Operation(summary = "Endpoint para excluir um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o deck foi excluido")
    })
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@Parameter(description = "identificador do deck", example = "62bdec5e5a8f2441c4d27460") @PathVariable
                      @Valid @MongoId(message = "{deckController.id}") String id);
}
