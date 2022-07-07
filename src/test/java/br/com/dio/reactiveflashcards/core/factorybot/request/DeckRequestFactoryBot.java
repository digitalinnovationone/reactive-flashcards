package br.com.dio.reactiveflashcards.core.factorybot.request;

import br.com.dio.reactiveflashcards.api.contorller.request.CardRequest;
import br.com.dio.reactiveflashcards.api.contorller.request.DeckRequest;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DeckRequestFactoryBot {

    public static DeckRequestFactoryBotBuilder builder(){
        return new DeckRequestFactoryBotBuilder();
    }

    public static class DeckRequestFactoryBotBuilder{
        private String name;
        private String description;
        private Set<CardRequest> cards = new HashSet<>();
        private final Faker faker = getFaker();


        public DeckRequestFactoryBotBuilder() {
            this.name = faker.name().name();
            generateCards(faker.number().numberBetween(3, 8));
            this.description = faker.color().name();
        }

        private void generateCards(final int amount){
            Set<String> front = new HashSet<>();
            while (front.size() != amount){
                front.add(faker.cat().name());
            }
            Set<String> back = new HashSet<>();
            while (back.size() != amount){
                back.add(faker.color().name());
            }
            var frontList = front.stream().toList();
            var backList = back.stream().toList();
            for (int i = 0; i < frontList.size(); i++) {
                cards.add(CardRequest.builder()
                        .front(frontList.get(i))
                        .back(backList.get(i))
                        .build());
            }
        }

        public DeckRequestFactoryBotBuilder blankName(){
            this.name = faker.bool().bool() ? null : " ";
            return this;
        }

        public DeckRequestFactoryBotBuilder longName(){
            this.name = faker.lorem().sentence(256);
            return this;
        }

        public DeckRequestFactoryBotBuilder blankDescription(){
            this.description = faker.bool().bool() ? null : " ";
            return this;
        }

        public DeckRequestFactoryBotBuilder longDescription(){
            this.description = faker.lorem().sentence(256);
            return this;
        }

        public DeckRequestFactoryBotBuilder nullCards(){
            this.cards = null;
            return this;
        }

        public DeckRequestFactoryBotBuilder lessThanThreeCards(){
            cards.clear();
            generateCards(faker.number().numberBetween(1, 3));
            return this;
        }

        public DeckRequestFactoryBotBuilder cardWithBlankFront(){
            this.cards.add(CardRequest.builder().front(faker.bool().bool() ? null : " ").build());
            return this;
        }

        public DeckRequestFactoryBotBuilder cardWithLongFront(){
            this.cards.add(CardRequest.builder().front(faker.lorem().sentence(256)).build());
            return this;
        }

        public DeckRequestFactoryBotBuilder cardWithBlankBack(){
            this.cards.add(CardRequest.builder().back(faker.bool().bool() ? null : " ").build());
            return this;
        }

        public DeckRequestFactoryBotBuilder cardWithLongBack(){
            this.cards.add(CardRequest.builder().back(faker.lorem().sentence(256)).build());
            return this;
        }

        public DeckRequest build(){
            return DeckRequest.builder()
                    .name(name)
                    .description(description)
                    .cards(cards)
                    .build();
        }

    }

}
