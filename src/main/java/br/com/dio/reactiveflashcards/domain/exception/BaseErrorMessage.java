package br.com.dio.reactiveflashcards.domain.exception;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    private final String DEFAULT_RESOURCE = "messages";

    public static final BaseErrorMessage GENERIC_EXCEPTION = new BaseErrorMessage("generic");
    public static final BaseErrorMessage GENERIC_NOT_FOUND = new BaseErrorMessage("generic.notFound");
    public static final BaseErrorMessage GENERIC_METHOD_NOT_ALLOW = new BaseErrorMessage("generic.methodNotAllow");
    public static final BaseErrorMessage GENERIC_BAD_REQUEST = new BaseErrorMessage("generic.badRequest");
    public static final BaseErrorMessage USER_NOT_FOUND = new BaseErrorMessage("user.NotFound");
    public static final BaseErrorMessage DECK_NOT_FOUND = new BaseErrorMessage("deck.NotFound");
    public static final BaseErrorMessage EMAIL_ALREADY_USED = new BaseErrorMessage("user.EmailAlreadyUsed");
    public static final BaseErrorMessage STUDY_NOT_FOUND = new BaseErrorMessage("study.NotFound");
    public static final BaseErrorMessage DECK_IN_STUDY = new BaseErrorMessage("study.DeckInStudy");

    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params){
        this.params = ArrayUtils.clone(params);
        return this;
    }

    public String getMessage(){
        var message = tryGetMessageFromBundle();
        if(ArrayUtils.isNotEmpty(params)){
            final var fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }

    private String tryGetMessageFromBundle(){
        return getResource().getString(key);
    }

    public ResourceBundle getResource(){
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}
