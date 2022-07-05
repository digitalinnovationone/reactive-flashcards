package br.com.dio.reactiveflashcards.utils.request;

import lombok.AllArgsConstructor;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
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

    public AbstractBodyAssertUtils<B> HttpStatusIsOk(){
        assertThat(response.getStatus()).isEqualTo(OK);
        return this;
    }

    public AbstractBodyAssertUtils<B> HttpStatusCreated(){
        assertThat(response.getStatus()).isEqualTo(CREATED);
        return this;
    }

    public AbstractBodyAssertUtils<B> HttpStatusNoContent(){
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT);
        return this;
    }

    public AbstractBodyAssertUtils<B> HttpStatusBadRequest(){
        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
        return this;
    }


}
