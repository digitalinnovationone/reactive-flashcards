package br.com.dio.reactiveflashcards.api.contorller.documentation;

import br.com.dio.reactiveflashcards.api.contorller.request.AnswerQuestionRequest;
import br.com.dio.reactiveflashcards.api.contorller.request.StudyRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.AnswerQuestionResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.QuestionResponse;
import br.com.dio.reactiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Study", description = "Endpoints para gerenciar estudos")
public interface StudyControllerDoc {

    @Operation(summary = "Inicia o estudo de um deck")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "O estudo foi criado e retorna a primeira pergunta gerada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<QuestionResponse> start(@Valid @RequestBody StudyRequest request);

    @Operation(summary = "Busca a ultima pergunta não respondida")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retorna a ultima pergunta que não foi respondida",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = QuestionResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}/current-question")
    Mono<QuestionResponse> getCurrentQuestion(@Parameter(description = "identificador do estudo", example = "62bdec5e5a8f2441c4d27460") @Valid
                                              @PathVariable @MongoId(message = "{studyController.id}") String id);


    @Operation(summary = "Responde a pergunta atual")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retorna a pergunta, a resposta fornecida e a resposta esperada",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AnswerQuestionResponse.class))})
    })
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE, value = "{id}/answer")
    Mono<AnswerQuestionResponse> answer(@Parameter(description = "identificador do estudo", example = "62bdec5e5a8f2441c4d27460") @Valid
                                        @PathVariable @MongoId(message = "{studyController.id}") String id,
                                        @Valid @RequestBody AnswerQuestionRequest request);
}
