package br.com.dio.reactiveflashcards.api.controller.deck;

import br.com.dio.reactiveflashcards.api.contorller.DeckController;
import br.com.dio.reactiveflashcards.api.contorller.response.DeckResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.DeckMapperImpl;
import br.com.dio.reactiveflashcards.domain.service.DeckService;
import br.com.dio.reactiveflashcards.domain.service.query.DeckQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.deckResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;

@ContextConfiguration(classes = {DeckMapperImpl.class})
@WebFluxTest(DeckController.class)
public class DeckControllerUpdateTest extends AbstractControllerTest {

    @MockBean
    public DeckService deckService;
    @MockBean
    public DeckQueryService deckQueryService;
    private RequestBuilder<DeckResponse> deckResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        deckResponseRequestBuilder = deckResponseRequestBuilder(applicationContext, "/decks");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/decks");
    }

}
