package br.com.dio.reactiveflashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("deck-api")
@ConstructorBinding
public record DeckApiConfig(String baseUrl,
                            String authResource,
                            String decksResource) {

    public String getAuthUri(){
        return String.format("%s/%s", baseUrl, authResource);
    }

    public String getDecksUri(){
        return String.format("%s/%s", baseUrl, decksResource);
    }

}
