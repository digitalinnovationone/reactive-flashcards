package br.com.dio.reactiveflashcards.core.factorybot.request;

import br.com.dio.reactiveflashcards.api.contorller.request.UserRequest;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserRequestFactoryBot {

    public static UserRequestFactoryBotBuilder builder(){
        return new UserRequestFactoryBotBuilder();
    }

    public static class UserRequestFactoryBotBuilder{

        private String name;
        private String email;
        private final Faker faker = getFaker();

        public UserRequestFactoryBotBuilder() {
            this.name = faker.name().name();
            this.email = faker.internet().emailAddress();
        }

        public UserRequestFactoryBotBuilder blankName(){
            this.name = faker.bool().bool() ? null : " ";
            return this;
        }

        public UserRequestFactoryBotBuilder longName(){
            this.name = faker.lorem().sentence(256);
            return this;
        }

        public UserRequestFactoryBotBuilder blankEmail(){
            this.email = faker.bool().bool() ? null : " ";
            return this;
        }

        public UserRequestFactoryBotBuilder longEmail(){
            this.email = faker.lorem().sentence(256);
            return this;
        }

        public UserRequestFactoryBotBuilder invalidEmail(){
            this.email = faker.lorem().word();
            return this;
        }

        public UserRequest build(){
            return UserRequest.builder()
                    .name(name)
                    .email(email)
                    .build();
        }

    }

}
