package br.com.dio.reactiveflashcards.domain.dto;

import br.com.dio.reactiveflashcards.domain.document.UserDocument;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public record UserPageDocument(Long currentPage,
                               Long totalPages,
                               Long totalItems,
                               List<UserDocument> content) {

    public static UserPageDocumentBuilder builder(){
        return new UserPageDocumentBuilder();
    }

    public UserPageDocumentBuilder toBuilder(final Integer limit){
        return new UserPageDocumentBuilder(limit, currentPage, totalItems, content);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPageDocumentBuilder {
        private Integer limit;
        private Long currentPage;
        private Long totalItems;
        private List<UserDocument> content;

        public UserPageDocumentBuilder limit(final Integer limit){
            this.limit = limit;
            return this;
        }

        public UserPageDocumentBuilder currentPage(final Long currentPage){
            this.currentPage = currentPage;
            return this;
        }

        public UserPageDocumentBuilder totalItems(final Long totalItems){
            this.totalItems = totalItems;
            return this;
        }

        public UserPageDocumentBuilder content(final List<UserDocument> content){
            this.content = content;
            return this;
        }

        public UserPageDocument build(){
            var totalPages = (totalItems / limit) + ((totalItems % limit > 0) ? 1 : 0);
            return new UserPageDocument(currentPage, totalPages, totalItems, content);
        }

    }

}
