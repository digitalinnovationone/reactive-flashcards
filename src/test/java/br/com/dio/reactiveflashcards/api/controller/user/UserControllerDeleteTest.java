package br.com.dio.reactiveflashcards.api.controller.user;

import br.com.dio.reactiveflashcards.api.contorller.UserController;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.UserMapperImpl;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.service.UserService;
import br.com.dio.reactiveflashcards.domain.service.query.UserQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.noContentRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {UserMapperImpl.class})
@WebFluxTest(UserController.class)
public class UserControllerDeleteTest extends AbstractControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;
    private RequestBuilder<Void> noContentRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        noContentRequestBuilder = noContentRequestBuilder(applicationContext, "/users");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users");
    }

    @Test
    void deleteTest(){
        when(userService.delete(anyString())).thenReturn(Mono.empty());
        noContentRequestBuilder.uri(uriBuilder -> uriBuilder
                .pathSegment("{id}")
                .build(ObjectId.get().toString()))
                .generateRequestWithoutBody()
                .doDelete()
                .httpStatusNoContent();
    }

    @Test
    void whenTryToDeleteNoStoredUserThenReturnNotFound(){
        when(userService.delete(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                .pathSegment("{id}")
                .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .httpStatusNotFound()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseInvalidIdThenReturnBadRequest(){
        when(userService.delete(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doDelete()
                .httpStatusBadRequest()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains("id");
                });
    }

}
