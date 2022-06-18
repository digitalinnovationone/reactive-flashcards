package br.com.dio.reactiveflashcards.api.exceptionhandler;

import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e ->
                        new MethodNotAllowHandler(mapper).handlerException(exchange, e))
                .onErrorResume(NotFoundException.class, e->
                        new NotFoundHandler(mapper).handlerException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e->
                        new ConstraintViolationHandler(mapper).handlerException(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e ->
                        new WebExchangeBindHandler(mapper, messageSource).handlerException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e ->
                        new ResponseStatusHandler(mapper).handlerException(exchange, e))
                .onErrorResume(ReactiveFlashcardsException.class, e ->
                        new ReactiveFlashcardsHandler(mapper).handlerException(exchange, e))
                .onErrorResume(Exception.class, e ->
                        new GenericHandler(mapper).handlerException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e ->
                        new JsonProcessingHandler(mapper).handlerException(exchange, e))
                .then();
    }

}
