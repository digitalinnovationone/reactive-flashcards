package br.com.dio.reactiveflashcards.api.contorller;

import br.com.dio.reactiveflashcards.api.contorller.documentation.StudyControllerDoc;
import br.com.dio.reactiveflashcards.api.contorller.request.AnswerQuestionRequest;
import br.com.dio.reactiveflashcards.api.contorller.request.StudyRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.AnswerQuestionResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.QuestionResponse;
import br.com.dio.reactiveflashcards.api.mapper.StudyMapper;
import br.com.dio.reactiveflashcards.core.validation.MongoId;
import br.com.dio.reactiveflashcards.domain.service.StudyService;
import br.com.dio.reactiveflashcards.domain.service.query.StudyQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("studies")
@Slf4j
@AllArgsConstructor
public class StudyController implements StudyControllerDoc {

    private final StudyService studyService;
    private final StudyQueryService studyQueryService;
    private final StudyMapper studyMapper;

    @Override
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<QuestionResponse> start(@Valid @RequestBody final StudyRequest request){
        return studyService.start(studyMapper.toDocument(request))
                .doFirst(() -> log.info("==== try to create a study with follow request {}", request))
                .map(document -> studyMapper.toResponse(document.getLastPendingQuestion(), document.id()));
    }

    @Override
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current-question")
    public Mono<QuestionResponse> getCurrentQuestion(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id){
        return studyQueryService.getLastPendingQuestion(id)
                .doFirst(() -> log.info("==== try to get a next question in stuudy {}", id))
                .map(question -> studyMapper.toResponse(question, id));
    }

    @Override
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "{id}/answer")
    public Mono<AnswerQuestionResponse> answer(@Valid @PathVariable @MongoId(message = "{studyController.id}") final String id,
                                               @Valid @RequestBody final AnswerQuestionRequest request){
        return studyService.answer(id, request.answer())
                .doFirst(() -> log.info("==== try to answer pending question in study {} with {}", id, request.answer()))
                .map(document -> studyMapper.toResponse(document.getLastAnsweredQuestion()));

    }

}
