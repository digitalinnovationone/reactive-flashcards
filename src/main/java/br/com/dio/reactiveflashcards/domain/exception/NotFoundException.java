package br.com.dio.reactiveflashcards.domain.exception;

public class NotFoundException extends ReactiveFlashcardsException {

    public NotFoundException(final String message) {
        super(message);
    }
}
