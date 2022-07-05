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
public class CollectionRequestBuilder<B> {

    private WebTestClient webTestClient;
    private Function<UriBuilder, URI> uriFunction;
    private Map<String, Set<String>> headers;
    private Object body;
    private Class<B> responseClass;

    public CollectionBodyAssertUtils<B> doGet(){
        var preResponse = webTestClient.get()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        var response = preResponse.exchange()
                .expectBodyList(responseClass)
                .returnResult();
        return new CollectionBodyAssertUtils<>(response);
    }

    public CollectionBodyAssertUtils<B> doPost(){
        var preResponse = webTestClient.post()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        if (Objects.nonNull(body)){
            return new CollectionBodyAssertUtils<>(preResponse.bodyValue(body).exchange().expectBodyList(responseClass).returnResult());
        }
        return new CollectionBodyAssertUtils<>(preResponse.exchange().expectBodyList(responseClass).returnResult());
    }

    public CollectionBodyAssertUtils<B> doPut(){
        var preResponse = webTestClient.put()
                .uri(uriFunction)
                .accept(APPLICATION_JSON);
        if (!headers.isEmpty()){
            headers.forEach((k, v) -> preResponse.header(k, v.toArray(String[]::new)));
        }
        if (Objects.nonNull(body)){
            return new CollectionBodyAssertUtils<>(preResponse.bodyValue(body).exchange().expectBodyList(responseClass).returnResult());
        }
        return new CollectionBodyAssertUtils<>(preResponse.exchange().expectBodyList(responseClass).returnResult());
    }

}
