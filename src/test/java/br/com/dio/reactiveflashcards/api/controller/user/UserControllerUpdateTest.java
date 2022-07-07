package br.com.dio.reactiveflashcards.api.controller.user;

import br.com.dio.reactiveflashcards.api.contorller.UserController;
import br.com.dio.reactiveflashcards.api.contorller.request.UserRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.UserResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.UserMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.request.UserRequestFactoryBot;
import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import br.com.dio.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.service.UserService;
import br.com.dio.reactiveflashcards.domain.service.query.UserQueryService;
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

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ContextConfiguration(classes = {UserMapperImpl.class})
@WebFluxTest(UserController.class)
public class UserControllerUpdateTest extends AbstractControllerTest {

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
    void updateTest(){
        when(userService.update(any(UserDocument.class))).thenAnswer(invocation -> {
            var document = invocation.getArgument(0, UserDocument.class);
            return Mono.just(document.toBuilder().updatedAt(OffsetDateTime.now()).build());
        });
        var request = UserRequestFactoryBot.builder().build();
        userResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .httpStatusIsOk()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(request);
                });
    }

    @Test
    void whenTryUpdateNonStoredUserThenReturnNotFound(){
        when(userService.update(any(UserDocument.class))).thenReturn(Mono.error(new NotFoundException("")));
        var request = UserRequestFactoryBot.builder().build();
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .httpStatusIsNotFound()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(NOT_FOUND.value());
                });
    }

    @Test
    void whenTryUseEmailInUseThenReturnConflict(){
        when(userService.update(any(UserDocument.class))).thenReturn(Mono.error(new EmailAlreadyUsedException("")));
        var request = UserRequestFactoryBot.builder().build();
        problemResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .pathSegment("{id}")
                        .build(ObjectId.get().toString()))
                .body(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .httpStatusIsBadRequest()
                .assertBody(response ->{
                    assertThat(response).isNotNull();
                    assertThat(response.status()).isEqualTo(BAD_REQUEST.value());
                });
    }

    private static Stream<Arguments> checkConstraintsTest(){
        Function<UriBuilder, URI> validId = uriBuilder -> uriBuilder
                .pathSegment("{id}")
                .build(ObjectId.get().toString());
        Function<UriBuilder, URI> invalidId = uriBuilder -> uriBuilder
                .pathSegment("{id}")
                .build(faker.lorem().word());
        return Stream.of(
                Arguments.of(UserRequestFactoryBot.builder().blankName().build(), "name", validId),
                Arguments.of(UserRequestFactoryBot.builder().longName().build(), "name", validId),
                Arguments.of(UserRequestFactoryBot.builder().blankEmail().build(), "email", validId),
                Arguments.of(UserRequestFactoryBot.builder().longEmail().build(), "email", validId),
                Arguments.of(UserRequestFactoryBot.builder().invalidEmail().build(), "email", validId),
                Arguments.of(UserRequestFactoryBot.builder().build(), "id", invalidId)
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintsTest(final UserRequest request, final String field, final Function<UriBuilder, URI> uriFunction){
        problemResponseRequestBuilder.uri(uriFunction)
                .body(request)
                .generateRequestWithSimpleBody()
                .doPut()
                .httpStatusIsBadRequest()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains(field);
                });
    }

}
