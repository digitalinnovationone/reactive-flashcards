package br.com.dio.reactiveflashcards.core.factorybot.request;

import br.com.dio.reactiveflashcards.api.contorller.request.StudyRequest;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class StudyRequestFactoryBot {

    public static StudyRequestFactoryBotBuilder builder(){
        return new StudyRequestFactoryBotBuilder();
    }

    public static class StudyRequestFactoryBotBuilder{
        private String deckId;
        private String userId;
        private final Faker faker = getFaker();

        public StudyRequestFactoryBotBuilder() {
            this.deckId = ObjectId.get().toString();
            this.userId = ObjectId.get().toString();
        }

        public StudyRequestFactoryBotBuilder invalidDeckId(){
            this.deckId = faker.bool().bool() ? null : faker.lorem().word();
            return this;
        }

        public StudyRequestFactoryBotBuilder invalidUserId(){
            this.userId = faker.bool().bool() ? null : faker.lorem().word();
            return this;
        }

        public StudyRequest build() {
            return StudyRequest.builder()
                    .deckId(deckId)
                    .userId(userId)
                    .build();
        }
    }
}
