package br.com.dio.reactiveflashcards.api.controller.deck;

import br.com.dio.reactiveflashcards.api.contorller.DeckController;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.DeckMapperImpl;
import br.com.dio.reactiveflashcards.domain.service.DeckService;
import br.com.dio.reactiveflashcards.domain.service.query.DeckQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.noContentRequestBuilder;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {DeckMapperImpl.class})
@WebFluxTest(DeckController.class)
public class DeckControllerSyncTest extends AbstractControllerTest {

    @MockBean
    public DeckService deckService;
    @MockBean
    public DeckQueryService deckQueryService;
    private RequestBuilder<Void> noContentRequestBuilder;

    @BeforeEach
    void setup(){
        noContentRequestBuilder = noContentRequestBuilder(applicationContext, "/decks");
    }

    @Test
    void syncTest(){
        when(deckService.sync()).thenReturn(Mono.empty());
        noContentRequestBuilder.uri(uriBuilder -> uriBuilder
                .pathSegment("sync")
                .build())
                .generateRequestWithoutBody()
                .doPost()
                .httpStatusIsOk();
    }

}
