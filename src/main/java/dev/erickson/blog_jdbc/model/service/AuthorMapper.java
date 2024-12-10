package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.model.AuthorEntity;

public class AuthorMapper {
    private AuthorMapper() {
    }

    public static Author toRest(final AuthorEntity authorEntity) {
        var builder = Author.builder();
        return builder
                .id(authorEntity.getId())
                .firstName(authorEntity.getFirstName())
                .lastName(authorEntity.getLastName())
                .email(authorEntity.getEmail())
                .username(authorEntity.getUsername()).build();
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
