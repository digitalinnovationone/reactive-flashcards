package br.com.dio.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
public class AbstractBodyAssertUtils<B> {

    private final EntityExchangeResult<B> response;

    public B getBody(){
        return response.getResponseBody();
    }

    public AbstractBodyAssertUtils<B> assertBody(final Consumer<B> consumer){
        consumer.accept(response.getResponseBody());
        return this;
    }

    public AbstractBodyAssertUtils<B> httpStatusIsOk(){
        assertThat(response.getStatus()).isEqualTo(OK);
        return this;
    }

    public AbstractBodyAssertUtils<B> httpStatusIsCreated(){
        assertThat(response.getStatus()).isEqualTo(CREATED);
        return this;
    }

    public AbstractBodyAssertUtils<B> httpStatusIsNoContent(){
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT);
        return this;
    }

    public AbstractBodyAssertUtils<B> httpStatusIsBadRequest(){
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        return this;
    }

    public AbstractBodyAssertUtils<B> httpStatusIsNotFound(){
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
        return this;
    }

    public AbstractBodyAssertUtils<B> httpStatusIsConflict(){
        assertThat(response.getStatus()).isEqualTo(CONFLICT);
        return this;
    }

}
