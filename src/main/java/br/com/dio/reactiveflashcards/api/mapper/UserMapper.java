package br.com.dio.reactiveflashcards.api.mapper;

import br.com.dio.reactiveflashcards.api.contorller.request.UserRequest;
import br.com.dio.reactiveflashcards.api.contorller.response.UserPageResponse;
import br.com.dio.reactiveflashcards.api.contorller.response.UserResponse;
import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import br.com.dio.reactiveflashcards.domain.dto.UserPageDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(final UserRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserDocument toDocument(final UserRequest request, final String id);

    UserResponse toResponse(final UserDocument document);

    UserPageResponse toResponse(final UserPageDocument document, final Integer limit);

}
