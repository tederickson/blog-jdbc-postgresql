package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.domain.Post;
import dev.erickson.blog_jdbc.model.PostEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostMapperTest {

    @Test
    void toRest() {
        Post post = PostMapper.toRest(PostEntity.builder().build());
        assertNotNull(post);
    }

    @Test
    void toEntity_missingAuthor() {
        assertThrows(IllegalArgumentException.class, () -> PostMapper.toEntity(Post.builder().build()));
    }

    @Test
    void toEntity() {
        PostEntity entity = PostMapper.toEntity(Post.builder()
                                                        .author(Author.builder().build())
                                                        .build());
        assertNotNull(entity);
    }
}