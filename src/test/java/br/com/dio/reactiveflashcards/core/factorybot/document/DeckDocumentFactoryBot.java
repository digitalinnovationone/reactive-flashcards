package br.com.dio.reactiveflashcards.core.factorybot.document;

import br.com.dio.reactiveflashcards.domain.document.Card;
import br.com.dio.reactiveflashcards.domain.document.DeckDocument;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DeckDocumentFactoryBot {

    public static DeckDocumentFactoryBotBuilder builder(){
        return new DeckDocumentFactoryBotBuilder();
    }

    public static class DeckDocumentFactoryBotBuilder{
        private String id;
        private String name;
        private String description;
        private final Set<Card> cards = new HashSet<>();
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private final Faker faker = getFaker();


        public DeckDocumentFactoryBotBuilder() {
            this.id = ObjectId.get().toString();
            this.name = faker.name().name();
            generateCards();
            this.description = faker.yoda().quote();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        private void generateCards(){
            cards.addAll(Stream.generate(() -> Card.builder()
                    .front(faker.cat().name())
                    .back(faker.color().name())
                    .build())
                    .limit(faker.number().numberBetween(3, 8))
                    .collect(Collectors.toSet()));
        }

        public DeckDocumentFactoryBotBuilder preInsert(){
            this.id = null;
            this.createdAt = null;
            this.updatedAt = null;
            return this;
        }

        public DeckDocumentFactoryBotBuilder preUpdate(final String id){
            this.id = id;
            return this;
        }

        public DeckDocument build(){
            return DeckDocument.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .cards(cards)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();
        }

    }

}
