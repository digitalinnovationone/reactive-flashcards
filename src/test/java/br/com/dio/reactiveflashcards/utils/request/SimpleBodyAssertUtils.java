package br.com.dio.reactiveflashcards.utils.request;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

public class SimpleBodyAssertUtils<B> extends AbstractBodyAssertUtils<B>{

    public SimpleBodyAssertUtils(final EntityExchangeResult<B> response) {
        super(response);
    }

}
