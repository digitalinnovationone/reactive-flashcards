package br.com.dio.reactiveflashcards.api.controller.study;

import br.com.dio.reactiveflashcards.api.contorller.StudyController;
import br.com.dio.reactiveflashcards.api.contorller.request.AnswerQuestionRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.AnswerQuestionResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.StudyMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.request.AnswerQuestionRequestFactoryBot;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.service.StudyService;
import br.com.dio.reactiveflashcards.domain.service.query.StudyQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.answerQuestionResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {StudyMapperImpl.class})
@WebFluxTest(StudyController.class)
public class StudyControllerAnswerTest extends AbstractControllerTest {

    @MockBean
    private StudyService studyService;
    @MockBean
    private StudyQueryService studyQueryService;

    private RequestBuilder<AnswerQuestionResponse> answerQuestionResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        answerQuestionResponseRequestBuilder = answerQuestionResponseRequestBuilder(applicationContext, "/studies");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/studies");
    }

    @Test
    void answerTest(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck)
                .pendingQuestions(1)
                .build();
        var request = AnswerQuestionRequestFactoryBot.builder().build();
        when(studyService.answer(anyString(), anyString())).thenReturn(Mono.just(study));
        answerQuestionResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                .pathSegment("{id}")
                .pathSegment("answer")
                .build(study.id()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsOk()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    var currentQuestion = study.getLastAnsweredQuestion();
                    assertThat(response.answered()).isEqualTo(currentQuestion.answered());
                    assertThat(response.answeredIn().toEpochSecond()).isEqualTo(currentQuestion.answeredIn().toEpochSecond());
                    assertThat(response.asked()).isEqualTo(currentQuestion.asked());
                    assertThat(response.askedIn().toEpochSecond()).isEqualTo(currentQuestion.askedIn().toEpochSecond());
                    assertThat(response.expected()).isEqualTo(currentQuestion.expected());
                });
    }

    @Test
    void whenNotFoundStudyOrStudyIsFinishedThenReturnNotFound(){
        var request = AnswerQuestionRequestFactoryBot.builder().build();
        when(studyService.answer(anyString(), anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("answer")
                        .build(ObjectId.get().toString()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsNotFound()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    private static Stream<Arguments> checkConstraintTest(){
        return Stream.of(
                Arguments.of(AnswerQuestionRequestFactoryBot.builder().build(),
                        faker.lorem().word(),
                        "id"),
                Arguments.of(AnswerQuestionRequestFactoryBot.builder().blankAnswer().build(),
                        ObjectId.get().toString(),
                        "answer"),
                Arguments.of(AnswerQuestionRequestFactoryBot.builder().longAnswer().build(),
                        ObjectId.get().toString(),
                        "answer")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintTest(final AnswerQuestionRequest request, final String studyId,
                             final String field){
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("answer")
                        .build(studyId))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsBadRequest()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains(field);
                });
    }

}
