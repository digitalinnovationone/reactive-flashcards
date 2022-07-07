package br.com.dio.reactiveflashcards.api.controller.deck;

import br.com.dio.reactiveflashcards.api.contorller.DeckController;
import br.com.dio.reactiveflashcards.api.contorller.request.DeckRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.DeckResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.DeckMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.request.DeckRequestFactoryBot;
import br.com.dio.reactiveflashcards.domain.document.DeckDocument;
import br.com.dio.reactiveflashcards.domain.service.DeckService;
import br.com.dio.reactiveflashcards.domain.service.query.DeckQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.deckResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ContextConfiguration(classes = {DeckMapperImpl.class})
@WebFluxTest(DeckController.class)
public class DeckControllerSaveTest  extends AbstractControllerTest {

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

    @Test
    void saveTest(){
        when(deckService.save(any(DeckDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, DeckDocument.class);
            return Mono.just(document.toBuilder()
                    .id(ObjectId.get().toString())
                    .createdAt(OffsetDateTime.now())
                    .updatedAt(OffsetDateTime.now())
                    .build());
        });
        var request = DeckRequestFactoryBot.builder().build();
        deckResponseRequestBuilder.uri(UriBuilder::build)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsCreated()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.id()).isNotNull();
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(request);
                });
    }

    private static Stream<Arguments> checkConstraintsTest(){
        return Stream.of(
                Arguments.of(DeckRequestFactoryBot.builder().blankName().build(), "name"),
                Arguments.of(DeckRequestFactoryBot.builder().longName().build(), "name"),
                Arguments.of(DeckRequestFactoryBot.builder().blankDescription().build(), "description"),
                Arguments.of(DeckRequestFactoryBot.builder().longDescription().build(), "description"),
                Arguments.of(DeckRequestFactoryBot.builder().nullCards().build(), "cards"),
                Arguments.of(DeckRequestFactoryBot.builder().lessThanThreeCards().build(), "cards"),
                Arguments.of(DeckRequestFactoryBot.builder().cardWithBlankFront().build(), "cards[].front"),
                Arguments.of(DeckRequestFactoryBot.builder().cardWithLongFront().build(), "cards[].front"),
                Arguments.of(DeckRequestFactoryBot.builder().cardWithBlankBack().build(), "cards[].back"),
                Arguments.of(DeckRequestFactoryBot.builder().cardWithLongBack().build(), "cards[].back")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintsTest(final DeckRequest request, final String field){
        problemResponseRequestBuilder.uri(UriBuilder::build)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsBadRequest()
                .assertBody(actual ->{
                        assertThat(actual).isNotNull();
                        assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                        assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains(field);
                    });
    }

}
