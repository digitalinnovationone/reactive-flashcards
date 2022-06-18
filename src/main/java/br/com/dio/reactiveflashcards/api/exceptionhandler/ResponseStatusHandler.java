package br.com.dio.reactiveflashcards.api.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;
import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
public class ResponseStatusHandler extends AbstractHandleException<ResponseStatusException>{

    public ResponseStatusHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handlerException(final ServerWebExchange exchange, final ResponseStatusException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, NOT_FOUND);
                    return GENERIC_NOT_FOUND.getMessage();
                }).map(message -> buildError(NOT_FOUND, message))
                .doFirst(() -> log.error("==== ResponseStatusException", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }
}
