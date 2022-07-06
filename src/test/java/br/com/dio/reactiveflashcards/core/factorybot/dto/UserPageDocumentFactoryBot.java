package br.com.dio.reactiveflashcards.core.factorybot.dto;

import br.com.dio.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import br.com.dio.reactiveflashcards.domain.dto.UserPageDocument;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserPageDocumentFactoryBot {

    public static UserPageDocumentFactoryBotBuilder builder(){
        return new UserPageDocumentFactoryBotBuilder();
    }

    public static class UserPageDocumentFactoryBotBuilder{
        private Long currentPage;
        private Integer limit;
        private Long totalItems;
        private List<UserDocument> content;
        private final Faker faker = getFaker();

        public UserPageDocumentFactoryBotBuilder() {
            this.currentPage = faker.number().numberBetween(1L, 20L);
            this.limit = faker.number().numberBetween(1, 10);
            var users = Stream.generate(() -> UserDocumentFactoryBot.builder().build())
                    .limit(limit)
                    .toList();
            this.content = users;
            this.totalItems = faker.number().numberBetween(users.size(), users.size() * 3L);
        }

        public UserPageDocumentFactoryBotBuilder emptyPage(){
            this.currentPage = 0L;
            this.limit = faker.number().numberBetween(5, 15);
            this.totalItems = 0L;
            this.content = new ArrayList<>();
            return this;
        }

        public UserPageDocument build(){
            return UserPageDocument.builder()
                    .currentPage(currentPage)
                    .totalItems(totalItems)
                    .content(content)
                    .limit(limit)
                    .build();
        }

    }

}
