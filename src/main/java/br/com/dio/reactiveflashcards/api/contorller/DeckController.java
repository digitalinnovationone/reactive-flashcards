package br.com.dio.reactiveflashcards.api.contorller;

import br.com.dio.reactiveflashcards.api.contorller.request.DeckRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.DeckResponse;
import br.com.dio.reactiveflashcards.api.mapper.DeckMapper;
import br.com.dio.reactiveflashcards.domain.service.DeckService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("decks")
@Slf4j
@AllArgsConstructor
public class DeckController {

    public final DeckService deckService;
    public final DeckMapper deckMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<DeckResponse> save(@Valid @RequestBody final DeckRequest request){
        return deckService.save(deckMapper.toDocument(request))
                .doFirst(() -> log.info("==== Saving a deck with follow data {}", request))
                .map(deckMapper::toResponse);
    }

}
