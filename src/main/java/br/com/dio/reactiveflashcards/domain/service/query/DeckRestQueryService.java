package br.com.dio.reactiveflashcards.domain.service.query;

import br.com.dio.reactiveflashcards.core.DeckApiConfig;
import br.com.dio.reactiveflashcards.domain.dto.DeckDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class DeckRestQueryService {

    private final WebClient webClient;
    private final DeckApiConfig deckApiConfig;

    public DeckRestQueryService(final WebClient webClient, final DeckApiConfig deckApiConfig) {
        this.webClient = webClient;
        this.deckApiConfig = deckApiConfig;
    }

    public Flux<DeckDTO> getDecks(){
        return Flux.empty();
    }

}
