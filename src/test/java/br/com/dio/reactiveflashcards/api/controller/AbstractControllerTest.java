package br.com.dio.reactiveflashcards.api.controller;

import br.com.dio.reactiveflashcards.ReactiveFlashcardsApplication;
import br.com.dio.reactiveflashcards.api.exceptionhandler.ApiExceptionHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.ConstraintViolationHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.DeckInStudyHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.EmailAlreadyUsedHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.GenericHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.JsonProcessingHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.MethodNotAllowHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.NotFoundHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.ReactiveFlashcardsHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.ResponseStatusHandler;
import br.com.dio.reactiveflashcards.api.exceptionhandler.WebExchangeBindHandler;
import br.com.dio.reactiveflashcards.core.mongo.provider.OffsetDateTimeProvider;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;

@ActiveProfiles("test")
@ContextConfiguration(classes = {OffsetDateTimeProvider.class, ApiExceptionHandler.class,
        DeckInStudyHandler.class, EmailAlreadyUsedHandler.class, MethodNotAllowHandler.class, NotFoundHandler.class,
        ConstraintViolationHandler.class, WebExchangeBindHandler.class, ResponseStatusHandler.class,
        ReactiveFlashcardsHandler.class, GenericHandler.class, JsonProcessingHandler.class, ReactiveFlashcardsApplication.class
})
public abstract class AbstractControllerTest {

    @MockBean
    protected MappingMongoConverter mappingMongoConverter;

    @Autowired
    protected ApplicationContext applicationContext;

    protected final Faker faker = getFaker();

}

