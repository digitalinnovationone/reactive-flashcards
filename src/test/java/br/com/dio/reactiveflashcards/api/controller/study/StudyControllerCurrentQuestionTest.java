package br.com.dio.reactiveflashcards.api.controller.study;

import br.com.dio.reactiveflashcards.api.contorller.StudyController;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.QuestionResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.StudyMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.service.StudyService;
import br.com.dio.reactiveflashcards.domain.service.query.StudyQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.questionResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {StudyMapperImpl.class})
@WebFluxTest(StudyController.class)
public class StudyControllerCurrentQuestionTest extends AbstractControllerTest {

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
    void getCurrentQuestion(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var question = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck)
                .pendingQuestions(1)
                .build().getLastPendingQuestion();
        when(studyQueryService.getLastPendingQuestion(anyString())).thenReturn(Mono.just(question));
        questionResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("current-question")
                .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(response ->assertThat(response).isNotNull());
    }

    @Test
    void whenTryToGetQuestionFromNonStoredStudyThenReturnNotFound(){
        when(studyQueryService.getLastPendingQuestion(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("current-question")
                        .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsNotFound()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseInvalidIdThenReturnBadRequest(){
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .pathSegment("current-question")
                        .build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsBadRequest()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains("id");
                });
    }

}
