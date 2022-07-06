package br.com.dio.reactiveflashcards.api.controller.user;

import br.com.dio.reactiveflashcards.api.contorller.UserController;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.UserResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.UserMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.document.UserDocumentFactoryBot;
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

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {UserMapperImpl.class})
@WebFluxTest(UserController.class)
public class UserControllerFindByIdTest extends AbstractControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;
    private RequestBuilder<UserResponse> userResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        userResponseRequestBuilder = userResponseRequestBuilder(applicationContext, "/users");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users");
    }

    @Test
    void findByIdTest(){
        var user = UserDocumentFactoryBot.builder().build();
        when(userQueryService.findById(anyString())).thenReturn(Mono.just(user));
        userResponseRequestBuilder
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(user.id()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("createdAt", "updatedAt")
                            .isEqualTo(user);
                });
    }


    @Test
    void whenTryToFindNonStoredUserThenReturnNotFound(){
        when(userQueryService.findById(anyString())).thenReturn(Mono.error(new NotFoundException("")));
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsNotFound()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseNonValidIdThenReturnBadRequest(){
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(faker.lorem().word()))
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsBadRequest()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(BAD_REQUEST.value());
                });
    }

}
