package br.com.dio.reactiveflashcards.domain.exception;

public class ReactiveFlashcardsException extends RuntimeException{

    public ReactiveFlashcardsException(final String message) {
        super(message);
    }

    public ReactiveFlashcardsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
