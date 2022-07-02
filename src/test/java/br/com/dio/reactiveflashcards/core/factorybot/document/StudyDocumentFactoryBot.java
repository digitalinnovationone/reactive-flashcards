package br.com.dio.reactiveflashcards.core.factorybot.document;

import br.com.dio.reactiveflashcards.domain.document.Card;
import br.com.dio.reactiveflashcards.domain.document.Question;
import br.com.dio.reactiveflashcards.domain.document.StudyDeck;
import br.com.dio.reactiveflashcards.domain.document.StudyDocument;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class StudyDocumentFactoryBot {

    public static StudyDocumentFactoryBotBuilder builder(final String userId, final String deckId, final Set<Card> cards){
        return new StudyDocumentFactoryBotBuilder(userId, deckId, cards);
    }

    public static class StudyDocumentFactoryBotBuilder{
        private String id;
        private String userId;
        private StudyDeck studyDeck = StudyDeck.builder().build();
        private List<Question> questions = new ArrayList<>();
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private Faker faker = getFaker();

        public StudyDocumentFactoryBotBuilder(final String userId, final String deckId, final Set<Card> cards) {
            this.id = ObjectId.get().toString();
            this.userId = userId;
            this.studyDeck = this.studyDeck.toBuilder()
                    .deckId(deckId)
                    .build();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        public StudyDocument build(){
            return StudyDocument.builder()
                    .id(id)
                    .userId(userId)
                    .studyDeck(studyDeck)
                    .questions(questions)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }

    }

}
