package br.com.dio.reactiveflashcards.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties("retry-config")
@ConstructorBinding
public record RetryConfig(Long maxRetries, Long minDuration) {

    public Duration minDurationSeconds(){
        return Duration.ofSeconds(minDuration);
    }

}
