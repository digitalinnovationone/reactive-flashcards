package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public abstract class AbstractHandleException<T extends Exception> {

    private final ObjectMapper mapper;

    abstract Mono<Void> handlerException(final ServerWebExchange exchange, final T ex);

    protected Mono<Void> writeResponse(final ServerWebExchange exchange, final ProblemResponse response) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> new DefaultDataBufferFactory().wrap(mapper.writeValueAsBytes(response))));
    }

    protected void prepareExchange(final ServerWebExchange exchange, final HttpStatus statusCode){
        exchange.getResponse().setStatusCode(statusCode);
        exchange.getResponse().getHeaders().setContentType(APPLICATION_JSON);
    }

    protected ProblemResponse buildError(final HttpStatus status, final String errorDescription){
        return ProblemResponse.builder()
                .status(status.value())
                .errorDescription(errorDescription)
                .timestamp(OffsetDateTime.now())
                .build();
    }

}
