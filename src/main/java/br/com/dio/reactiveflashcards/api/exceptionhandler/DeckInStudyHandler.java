package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.domain.exception.DeckInStudyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CONFLICT;

@Component
@Slf4j
public class DeckInStudyHandler extends AbstractHandleException<DeckInStudyException>{

    public DeckInStudyHandler(final ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    Mono<Void> handlerException(final ServerWebExchange exchange, final DeckInStudyException ex) {
        return Mono.fromCallable(() -> {
                    prepareExchange(exchange, CONFLICT);
                    return ex.getMessage();
                }).map(message -> buildError(CONFLICT, message))
                .doFirst(() -> log.error("==== NotFoundException", ex))
                .flatMap(response -> writeResponse(exchange, response));
    }
}
