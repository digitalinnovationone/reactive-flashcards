package br.com.dio.reactiveflashcards.core.factorybot.dto;

import br.com.dio.reactiveflashcards.domain.document.DeckDocument;
import br.com.dio.reactiveflashcards.domain.document.Question;
import br.com.dio.reactiveflashcards.domain.dto.MailMessageDTO;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MailMessageDTOFactoryBot {

    public static MailMessageDTOFactoryBotBuilder builder(final DeckDocument deck, final List<Question> questions){
        return new MailMessageDTOFactoryBotBuilder(deck, questions);
    }

    public static class MailMessageDTOFactoryBotBuilder{

        private String destination;
        private String subject;
        private Map<String, Object> variables = new HashMap<>();
        private String username;
        private final DeckDocument deck;
        private final List<Question> questions;

        public MailMessageDTOFactoryBotBuilder(final DeckDocument deck, final List<Question> questions) {
            var faker = getFaker();
            this.destination = faker.internet().emailAddress();
            this.subject = faker.chuckNorris().fact();
            this.username = faker.name().fullName();
            this.deck = deck;
            this.questions = questions;
        }

        public MailMessageDTO build(){
            return MailMessageDTO.builder()
                    .destination(destination)
                    .subject(subject)
                    .username(username)
                    .deck(deck)
                    .questions(questions)
                    .build();
        }

    }

}
