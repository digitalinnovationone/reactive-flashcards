package br.com.dio.reactiveflashcards.api.controller.study;

import br.com.dio.reactiveflashcards.api.contorller.StudyController;
import br.com.dio.reactiveflashcards.api.contorller.request.StudyRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.QuestionResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.StudyMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.request.StudyRequestFactoryBot;
import br.com.dio.reactiveflashcards.domain.document.StudyDocument;
import br.com.dio.reactiveflashcards.domain.exception.DeckInStudyException;
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
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.questionResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {StudyMapperImpl.class})
@WebFluxTest(StudyController.class)
public class StudyControllerStartTest extends AbstractControllerTest {

    @MockBean
    private StudyService studyService;
    @MockBean
    private StudyQueryService studyQueryService;

    private RequestBuilder<QuestionResponse> questionResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        questionResponseRequestBuilder = questionResponseRequestBuilder(applicationContext, "/studies");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/studies");
    }

    @Test
    void startTest(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck)
                .pendingQuestions(1)
                .build();
        var request = StudyRequestFactoryBot.builder().build();
        when(studyService.start(any(StudyDocument.class))).thenReturn(Mono.just(study));
        questionResponseRequestBuilder.uri(UriBuilder::build)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsCreated()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.id()).isEqualTo(study.id());
                    assertThat(response.asked()).isEqualTo(study.getLastPendingQuestion().asked());
                    assertThat(response.askedIn()).isEqualTo(study.getLastPendingQuestion().askedIn());
                });
    }

    @Test
    void whenHasOtherStudyWithSameDeckForUserThenReturnConflict(){
        when(studyService.start(any(StudyDocument.class))).thenReturn(Mono.error(new DeckInStudyException("")));
        var request = StudyRequestFactoryBot.builder().build();
        problemResponseRequestBuilder.uri(UriBuilder::build)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsConflict()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(CONFLICT.value());
                });
    }

    @Test
    void whenTryToStartStudyWithoutStoredDeckOrUSerThenReturnNotFound(){
        when(studyService.start(any(StudyDocument.class))).thenReturn(Mono.error(new NotFoundException("")));
        var request = StudyRequestFactoryBot.builder().build();
        problemResponseRequestBuilder.uri(UriBuilder::build)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsNotFound()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    private static Stream<Arguments> checkConstraintsTest(){
        return Stream.of(
                Arguments.of(StudyRequestFactoryBot.builder().invalidDeckId().build(), "deckId"),
                Arguments.of(StudyRequestFactoryBot.builder().invalidUserId().build(), "userId")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintsTest(final StudyRequest request, final String errorField){
        problemResponseRequestBuilder.uri(UriBuilder::build)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPost()
                .httpStatusIsBadRequest()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains(errorField);
                });
    }

}
