package br.com.dio.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@AllArgsConstructor
public class SimpleRequestBuilder<B> {

    private WebTestClient webTestClient;
    private Function<UriBuilder, URI> uriFunction;
    private Map<String, Set<String>> headers;
    private Object body;
    private Class<B> responseClass;

    public SimpleBodyAssertUtils<B> doGet(){
        var preResponse = webTestClient.get()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody(responseClass)
                .returnResult();
        return new SimpleBodyAssertUtils<>(response);
    }

    public SimpleBodyAssertUtils<B> doDelete(){
        var preResponse = webTestClient.get()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBody(responseClass)
                .returnResult();
        return new SimpleBodyAssertUtils<>(response);
    }

    public SimpleBodyAssertUtils<B> doPost(){
        var preResponse = webTestClient.post()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        if (Objects.nonNull(body)){
            return new SimpleBodyAssertUtils<>(preResponse.bodyValue(body).exchange().expectBody(responseClass).returnResult());
        }
        return new SimpleBodyAssertUtils<>(preResponse.exchange().expectBody(responseClass).returnResult());
    }

    public SimpleBodyAssertUtils<B> doPut(){
        var preResponse = webTestClient.put()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        if (Objects.nonNull(body)){
            return new SimpleBodyAssertUtils<>(preResponse.bodyValue(body).exchange().expectBody(responseClass).returnResult());
        }
        return new SimpleBodyAssertUtils<>(preResponse.exchange().expectBody(responseClass).returnResult());
    }

}
