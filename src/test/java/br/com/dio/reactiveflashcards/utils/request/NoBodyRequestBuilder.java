package br.com.dio.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@AllArgsConstructor
public class NoBodyRequestBuilder {

    private WebTestClient webTestClient;
    private Function<UriBuilder, URI> uriFunction;
    private Map<String, Set<String>> headers;
    private Object body;

    public EmptyBodyAssertUtils doDelete(){
        var preResponse = webTestClient.delete()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody()
                .isEmpty();
        return new EmptyBodyAssertUtils(response);
    }

    public EmptyBodyAssertUtils doPost(){
        var preResponse = webTestClient.post()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        if (Objects.nonNull(body)){
            return new EmptyBodyAssertUtils(preResponse.bodyValue(body).exchange().expectBody().isEmpty());
        }
        return new EmptyBodyAssertUtils(preResponse.exchange().expectBody().isEmpty());
    }

    public EmptyBodyAssertUtils doPut(){
        var preResponse = webTestClient.put()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        if (Objects.nonNull(body)){
            return new EmptyBodyAssertUtils(preResponse.bodyValue(body).exchange().expectBody().isEmpty());
        }
        return new EmptyBodyAssertUtils(preResponse.exchange().expectBody().isEmpty());
    }

}
