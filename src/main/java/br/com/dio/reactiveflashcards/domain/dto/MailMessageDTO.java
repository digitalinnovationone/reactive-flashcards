package br.com.dio.reactiveflashcards.domain.dto;

import br.com.dio.reactiveflashcards.domain.document.DeckDocument;
import br.com.dio.reactiveflashcards.domain.document.Question;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public record MailMessageDTO(String destination,
                             String subject,
                             String template,
                             Map<String, Object> variables) {

    public static MailMessageDTOBuilder builder(){
        return new MailMessageDTOBuilder();
    }

    public static class MailMessageDTOBuilder{
        private String destination;
        private String subject;
        private Map<String, Object> variables;

        public MailMessageDTOBuilder destination(final String destination){
            this.destination = destination;
            return this;
        }

        public MailMessageDTOBuilder subject(final String subject){
            this.subject = subject;
            return this;
        }

        private MailMessageDTOBuilder variables(final String key, final Object value){
            this.variables.put(key, value);
            return this;
        }

        public MailMessageDTOBuilder username(final String username){
            return variables("username", username);
        }

        public MailMessageDTOBuilder deck(final DeckDocument deck){
            return variables("deck", deck);
        }

        public MailMessageDTOBuilder questions(final List<Question> questions){
            questions.sort(Comparator.comparing(Question::answeredIn));
            return variables("questions", questions);
        }

        public MailMessageDTO build(){
            return new MailMessageDTO(destination, subject, "mail/studyResult", variables);
        }

    }

}
