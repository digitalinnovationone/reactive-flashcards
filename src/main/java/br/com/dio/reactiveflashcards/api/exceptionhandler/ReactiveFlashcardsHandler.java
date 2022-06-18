package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public class ReactiveFlashcardsHandler extends AbstractHandleException<ReactiveFlashcardsException>{

    public ReactiveFlashcardsHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handlerException(final ServerWebExchange exchange, final ReactiveFlashcardsException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== ReactiveFlashcardsException ", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }
}
