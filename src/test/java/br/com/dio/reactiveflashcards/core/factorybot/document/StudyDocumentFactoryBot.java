package br.com.dio.reactiveflashcards.core.factorybot.document;

import br.com.dio.reactiveflashcards.domain.document.DeckDocument;
import br.com.dio.reactiveflashcards.domain.document.Question;
import br.com.dio.reactiveflashcards.domain.document.StudyCard;
import br.com.dio.reactiveflashcards.domain.document.StudyDeck;
import br.com.dio.reactiveflashcards.domain.document.StudyDocument;
import com.github.javafaker.Faker;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static br.com.dio.reactiveflashcards.core.factorybot.RandomData.getFaker;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class StudyDocumentFactoryBot {

    public static StudyDocumentFactoryBotBuilder builder(final String userId, final DeckDocument deck){
        return new StudyDocumentFactoryBotBuilder(userId, deck);
    }

    public static class StudyDocumentFactoryBotBuilder{
        private String id;
        private String userId;
        private StudyDeck studyDeck = StudyDeck.builder().build();
        private List<Question> questions = new ArrayList<>();
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private Faker faker = getFaker();

        public StudyDocumentFactoryBotBuilder(final String userId, final DeckDocument deck) {
            this.id = ObjectId.get().toString();
            this.userId = userId;
            generateStudy(deck);
            generateRandomQuestionWithWrongAnswer();
            generateRandomQuestionWithRightAnswer();
            generateNonAskedRandomQuestion();
            this.createdAt = OffsetDateTime.now();
            this.updatedAt = OffsetDateTime.now();
        }

        public StudyDocumentFactoryBotBuilder finishedStudy(){
            this.questions.clear();
            studyDeck.cards().forEach(c -> questions.add(Question.builder()
                    .asked(c.front())
                    .answered(c.back())
                    .expected(c.back())
                    .build()));
            return this;
        }

        public StudyDocumentFactoryBotBuilder preInsert(){
            this.id = null;
            this.createdAt = null;
            this.updatedAt = null;
            generateNonAskedRandomQuestion();
            return this;
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

        private void generateNonAskedRandomQuestion(){
            var values = new ArrayList<>(studyDeck.cards());
            var random = new Random();
            var position = random.nextInt(values.size());
            var card = values.get(position);
            questions.add(Question.builder().asked(card.front()).expected(card.back()).build());
        }

        private void generateRandomQuestionWithWrongAnswer(){
            var values = new ArrayList<>(studyDeck.cards());
            var random = new Random();
            var position = random.nextInt(values.size());
            var card = values.get(position);
            questions.add(Question.builder()
                    .asked(card.front())
                    .answered(faker.app().name())
                    .expected(card.back())
                    .build());
        }

        private void generateRandomQuestionWithRightAnswer(){
            var values = new ArrayList<>(studyDeck.cards());
            var random = new Random();
            var position = random.nextInt(values.size());
            var card = values.get(position);
            questions.add(Question.builder()
                    .asked(card.front())
                    .answered(card.back())
                    .expected(card.back())
                    .build());
        }

        private void generateStudy(final DeckDocument deck) {
            var studyCards = deck.cards().stream().map(c -> StudyCard.builder()
                    .front(c.front())
                    .back(c.back())
                    .build()).collect(Collectors.toSet());
            this.studyDeck = this.studyDeck.toBuilder()
                    .deckId(deck.id())
                    .cards(studyCards)
                    .build();
        }

    }

}
