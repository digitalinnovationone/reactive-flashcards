package br.com.dio.reactiveflashcards.api.contorller.documentation;

import br.com.dio.reactiveflashcards.api.contorller.request.UserPageRequest;
import br.com.dio.reactiveflashcards.api.contorller.request.UserRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.UserPageResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.UserResponse;
import br.com.dio.reactiveflashcards.core.validation.MongoId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "User", description = "Endpoints para manipulação de usuários")
public interface UserControllerDoc {

    @Operation(summary = "Endpoint para criar um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "retornar o usuário criado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    Mono<UserResponse> save(@Valid @RequestBody UserRequest request);

    @Operation(summary = "Endpoint para buscar um usuário pelo seu identificador")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o usuário correspondete ao identificador",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> findById(@Parameter(description = "identificador do usuário", example = "62bdec5e5a8f2441c4d27460") @PathVariable
                                @Valid @MongoId(message = "{userController.id}") String id);

    @Operation(summary = "Endpoint para buscar usuários de forma paginada")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar os usuários de acordo com as informações passadas na request",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserPageResponse.class))})
    })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Mono<UserPageResponse> findOnDemand(@Valid UserPageRequest request);

    @Operation(summary = "Endpoint para atualizar um usuário")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "retornar o usuário atualizado",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))})
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, value = "{id}")
    Mono<UserResponse> update(@Parameter(description = "identificador do usuário", example = "62bdec5e5a8f2441c4d27460") @PathVariable
                              @Valid @MongoId(message = "{userController.id}") String id,
                              @Valid @RequestBody UserRequest request);

    @Operation(summary = "Endpoint para excluir um usuário")
        @ApiResponses({
            @ApiResponse(responseCode = "204", description = "o usuário foi excluido")
    })
    @DeleteMapping(value = "{id}")
    @ResponseStatus(NO_CONTENT)
    Mono<Void> delete(@Parameter(description = "identificador do usuário", example = "62bdec5e5a8f2441c4d27460") @PathVariable
                      @Valid @MongoId(message = "{userController.id}") String id);
}
