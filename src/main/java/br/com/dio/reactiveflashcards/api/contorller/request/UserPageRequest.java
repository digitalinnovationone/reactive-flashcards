package br.com.dio.reactiveflashcards.api.contorller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

import static br.com.dio.reactiveflashcards.api.contorller.request.UserSortBy.NAME;
import static br.com.dio.reactiveflashcards.api.contorller.request.UserSortDirection.ASC;
import static br.com.dio.reactiveflashcards.api.contorller.request.UserSortDirection.DESC;

public record UserPageRequest(@JsonProperty("sentence")
                              String sentence,
                              @PositiveOrZero
                              @JsonProperty("page")
                              Long page,
                              @Min(1)
                              @Max(50)
                              @JsonProperty("limit")
                              Integer limit,
                              @JsonProperty("sortBy")
                              UserSortBy sortBy,
                              @JsonProperty("sortDirection")
                              UserSortDirection sortDirection) {

    @Builder(toBuilder = true)
    public UserPageRequest {
        if (Objects.isNull(sortBy)){
            sortBy = NAME;
        }
        if (Objects.isNull(sortDirection)){
            sortDirection = ASC;
        }
        limit = 20;
        page = 0L;
    }

    public Sort getSort(){
        return sortDirection.equals(DESC) ? Sort.by(sortBy.getField()).descending() : Sort.by(sortBy.getField()).ascending();
    }

    public Long getSkip(){
        return page > 0 ? ((page - 1) * limit) :0;
    }

}
