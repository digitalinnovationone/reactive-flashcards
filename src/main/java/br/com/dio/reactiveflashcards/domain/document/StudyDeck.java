package br.com.dio.reactiveflashcards.domain.document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

public record StudyDeck(String deckId,
                        Set<StudyCard> cards) {

    public static StudyDeckBuilder builder(){
        return new StudyDeckBuilder();
    }

    public StudyDeckBuilder toBuilder(){
        return new StudyDeckBuilder(deckId, cards);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudyDeckBuilder{
        private String deckId;
        private Set<StudyCard> cards = new HashSet<>();

        public StudyDeckBuilder deckId(final String deckId){
            this.deckId = deckId;
            return this;
        }

        public StudyDeckBuilder cards(final Set<StudyCard> cards){
            this.cards = cards;
            return this;
        }

        public StudyDeck build(){
            return new StudyDeck(deckId, cards);
        }

    }

}
