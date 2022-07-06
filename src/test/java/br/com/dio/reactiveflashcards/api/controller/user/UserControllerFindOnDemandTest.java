package br.com.dio.reactiveflashcards.api.controller.user;

import br.com.dio.reactiveflashcards.api.contorller.UserController;
import br.com.dio.reactiveflashcards.api.contorller.request.UserPageRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.ErrorFieldResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.ProblemResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.UserPageResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.UserResponse;
import br.com.dio.reactiveflashcards.api.controller.AbstractControllerTest;
import br.com.dio.reactiveflashcards.api.mapper.UserMapperImpl;
import br.com.dio.reactiveflashcards.core.factorybot.dto.UserPageDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.request.UserPageRequestFactoryBot;
import br.com.dio.reactiveflashcards.domain.dto.UserPageDocument;
import br.com.dio.reactiveflashcards.domain.service.UserService;
import br.com.dio.reactiveflashcards.domain.service.query.UserQueryService;
import br.com.dio.reactiveflashcards.utils.request.RequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.problemResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.userPageResponseRequestBuilder;
import static br.com.dio.reactiveflashcards.utils.request.RequestBuilder.userResponseRequestBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ContextConfiguration(classes = {UserMapperImpl.class})
@WebFluxTest(UserController.class)
public class UserControllerFindOnDemandTest extends AbstractControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserQueryService userQueryService;
    private RequestBuilder<UserPageResponse> userPageResponseRequestBuilder;
    private RequestBuilder<ProblemResponse> problemResponseRequestBuilder;

    @BeforeEach
    void setup(){
        userPageResponseRequestBuilder = userPageResponseRequestBuilder(applicationContext, "/users");
        problemResponseRequestBuilder = problemResponseRequestBuilder(applicationContext, "/users");
    }

    private static Stream<UserPageDocument> findOnDemandTest(){
        return Stream.of(
                UserPageDocumentFactoryBot.builder().build(),
                UserPageDocumentFactoryBot.builder().emptyPage().build()
        );
    }

    @ParameterizedTest
    @MethodSource
    void findOnDemandTest(final UserPageDocument pageDocument){
        var queryParams = UserPageRequestFactoryBot.builder().build();
        when(userQueryService.findOnDemand(any(UserPageRequest.class))).thenReturn(Mono.just(pageDocument));
        userPageResponseRequestBuilder.uri(uriBuilder -> uriBuilder
                        .queryParam("page", queryParams.page())
                        .queryParam("limit", queryParams.limit())
                        .queryParam("sentence", queryParams.sentence())
                        .queryParam("sortBy", queryParams.sortBy())
                        .queryParam("sortDirection", queryParams.sortDirection())
                        .build())
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsOk()
                .assertBody(response -> assertThat(response).isNotNull());
    }

    private static Stream<Arguments> checkConstraintsTest(){
        var invalidPageQueryParam = UserPageRequestFactoryBot.builder().negativePage().build();
        Function<UriBuilder, URI> invalidPage = uriBuilder -> uriBuilder
                .queryParam("page", invalidPageQueryParam.page())
                .queryParam("limit", invalidPageQueryParam.limit())
                .queryParam("sentence", invalidPageQueryParam.sentence())
                .queryParam("sortBy", invalidPageQueryParam.sortBy())
                .queryParam("sortDirection", invalidPageQueryParam.sortDirection())
                .build();
        var lessZeroLimitQueryParam = UserPageRequestFactoryBot.builder().lessThanZeroLimit().build();
        Function<UriBuilder, URI> lessZeroLimit = uriBuilder -> uriBuilder
                .queryParam("page", lessZeroLimitQueryParam.page())
                .queryParam("limit", lessZeroLimitQueryParam.limit())
                .queryParam("sentence", lessZeroLimitQueryParam.sentence())
                .queryParam("sortBy", lessZeroLimitQueryParam.sortBy())
                .queryParam("sortDirection", lessZeroLimitQueryParam.sortDirection())
                .build();
        var greaterFiftyQueryParam = UserPageRequestFactoryBot.builder().greaterThanFiftyLimit().build();
        Function<UriBuilder, URI> greaterFifty = uriBuilder -> uriBuilder
                .queryParam("page", greaterFiftyQueryParam.page())
                .queryParam("limit", greaterFiftyQueryParam.limit())
                .queryParam("sentence", greaterFiftyQueryParam.sentence())
                .queryParam("sortBy", greaterFiftyQueryParam.sortBy())
                .queryParam("sortDirection", greaterFiftyQueryParam.sortDirection())
                .build();
        return Stream.of(
                Arguments.of(invalidPage, "page"),
                Arguments.of(lessZeroLimit, "limit"),
                Arguments.of(greaterFifty, "limit")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkConstraintsTest(final Function<UriBuilder, URI> uriFunction, final String field){
        problemResponseRequestBuilder.uri(uriFunction)
                .generateRequestWithSimpleBody()
                .doGet()
                .httpStatusIsBadRequest()
                .assertBody(actual ->{
                    assertThat(actual).isNotNull();
                    assertThat(actual.status()).isEqualTo(BAD_REQUEST.value());
                    assertThat(actual.fields().stream().map(ErrorFieldResponse::name).toList()).contains(field);
                });
    }
}
