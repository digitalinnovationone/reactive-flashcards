package br.com.dio.reactiveflashcards.domain.exception;

public class EmailAlreadyUsedException extends ReactiveFlashcardsException{

    public EmailAlreadyUsedException(final String message) {
        super(message);
    }

}
