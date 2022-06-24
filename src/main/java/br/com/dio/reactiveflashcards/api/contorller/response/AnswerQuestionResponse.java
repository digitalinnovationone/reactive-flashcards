package br.com.dio.reactiveflashcards.api.contorller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.Objects;

public record AnswerQuestionResponse(@JsonProperty("asked")
                                     String asked,
                                     @JsonProperty("askedIn")
                                     OffsetDateTime askedIn,
                                     @JsonProperty("answered")
                                     String answered,
                                     @JsonProperty("answeredIn")
                                     OffsetDateTime answeredIn,
                                     @JsonProperty("expected")
                                     String expected) {

    @Builder(toBuilder = true)
    public AnswerQuestionResponse { }
}
