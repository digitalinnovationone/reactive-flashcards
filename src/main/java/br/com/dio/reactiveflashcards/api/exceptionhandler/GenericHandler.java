package br.com.dio.reactiveflashcards.api.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public class GenericHandler extends AbstractHandleException<Exception>{

    public GenericHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handlerException(final ServerWebExchange exchange, final Exception ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, INTERNAL_SERVER_ERROR);
                    return GENERIC_EXCEPTION.getMessage();
                }).map(message -> buildError(INTERNAL_SERVER_ERROR, message))
                .doFirst(() -> log.error("==== Exception ", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }
}
