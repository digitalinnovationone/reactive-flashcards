package br.com.dio.reactiveflashcards.domain.service.query;

import br.com.dio.reactiveflashcards.core.DeckApiConfig;
import br.com.dio.reactiveflashcards.core.extension.server.WebServer;
import br.com.dio.reactiveflashcards.core.extension.server.WebServerExtension;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static br.com.dio.reactiveflashcards.utils.MockWebServerUtils.getListJson;
import static br.com.dio.reactiveflashcards.utils.MockWebServerUtils.getSimpleJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles("test")
@ExtendWith(WebServerExtension.class)
public class DeckRestQueryServiceTest {

    private final WebClient webClient = WebClient.builder().build();

    private DeckRestQueryService deckRestQueryService;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setup(@WebServer final MockWebServer mockWebServer){
        this.mockWebServer = mockWebServer;
        var deckApiConfig = new DeckApiConfig(mockWebServer.url("/").toString(), "auth", "decks");
        deckRestQueryService = new DeckRestQueryService(webClient, deckApiConfig);
    }

    @Test
    void getDecksTest(){
        var authResponse = new MockResponse();
        authResponse.setResponseCode(OK.value());
        authResponse.setBody(getSimpleJson("authResponse"));
        authResponse.addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        var decksResponse = new MockResponse();
        decksResponse.setResponseCode(OK.value());
        decksResponse.setBody(getListJson("decksResponse"));
        decksResponse.addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        var decksCacheResponse = new MockResponse();
        decksCacheResponse.setResponseCode(OK.value());
        decksCacheResponse.setBody(getListJson("decksResponse"));
        decksCacheResponse.addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        mockWebServer.enqueue(authResponse);
        mockWebServer.enqueue(decksResponse);
        mockWebServer.enqueue(decksCacheResponse);
        StepVerifier.create(deckRestQueryService.getDecks())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(actual -> true)
                .consumeRecordedWith(actual -> actual.forEach(a -> assertThat(a).hasNoNullFieldsOrProperties()))
                .verifyComplete();
        StepVerifier.create(deckRestQueryService.getDecks())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(actual -> true)
                .consumeRecordedWith(actual -> actual.forEach(a -> assertThat(a).hasNoNullFieldsOrProperties()))
                .verifyComplete();
    }

}
