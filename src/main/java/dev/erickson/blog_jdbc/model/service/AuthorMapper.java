package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.model.AuthorEntity;

public class AuthorMapper {
    private AuthorMapper() {
    }

    public static Author toRest(final AuthorEntity persistAuthorEntity) {
        var builder = Author.builder();
        return builder
                .id(persistAuthorEntity.getId())
                .firstName(persistAuthorEntity.getFirstName())
                .lastName(persistAuthorEntity.getLastName())
                .email(persistAuthorEntity.getEmail())
                .username(persistAuthorEntity.getUsername()).build();
    }

    public static AuthorEntity toEntity(final Author author) {
        var builder = AuthorEntity.builder();
        return builder
                .id(author.id())
                .firstName(author.firstName())
                .lastName(author.lastName())
                .email(author.email())
                .username(author.username()).build();
    }
}
