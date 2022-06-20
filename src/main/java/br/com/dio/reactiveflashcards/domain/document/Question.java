package br.com.dio.reactiveflashcards.domain.document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;

public record Question(String asked,
                       @Field("asked_in")
                       OffsetDateTime askedIn,
                       String answered,
                       @Field("answered_in")
                       OffsetDateTime answeredIn,
                       String expected) {

    public static QuestionBuilder builder(){
        return new QuestionBuilder();
    }

    public QuestionBuilder toBuilder(){
        return new QuestionBuilder(asked, askedIn, answered, answeredIn, expected);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionBuilder{
        private String asked;
        private OffsetDateTime askedIn;
        private String answered;
        private OffsetDateTime answeredIn;
        private String expected;

        public QuestionBuilder asked(final String asked){
            this.asked = asked;
            return this;
        }

        public QuestionBuilder askedIn(final OffsetDateTime askedIn){
            this.askedIn = askedIn;
            return this;
        }

        public QuestionBuilder answered(final String answered){
            this.answered = answered;
            return this;
        }

        public QuestionBuilder answeredIn(final OffsetDateTime answeredIn){
            this.answeredIn = answeredIn;
            return this;
        }

        public QuestionBuilder expected(final String expected){
            this.expected = expected;
            return this;
        }

        public Question build(){
            return new Question(asked, askedIn, answered, answeredIn, expected);
        }
    }

}
