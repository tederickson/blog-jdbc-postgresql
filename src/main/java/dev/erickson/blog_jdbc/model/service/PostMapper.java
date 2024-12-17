package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Post;
import dev.erickson.blog_jdbc.model.PostEntity;
import org.springframework.util.Assert;

public class PostMapper {
    private PostMapper() {
    }

    public static Post toRest(final PostEntity persistPostEntity) {
        var builder = Post.builder();
        return builder
                .id(persistPostEntity.getId())
                .title(persistPostEntity.getTitle())
                .content(persistPostEntity.getContent())
                .publishedOn(persistPostEntity.getPublishedOn())
                .updatedOn(persistPostEntity.getUpdatedOn())
                .build();
    }

    public static PostEntity toEntity(final Post post) {
        Assert.notNull(post.getAuthor(), "The author must not be null");

        var builder = PostEntity.builder();
        return builder
                .id(post.getId())
                .authorId(post.getAuthor().id())
                .title(post.getTitle())
                .content(post.getContent())
                .publishedOn(post.getPublishedOn())
                .updatedOn(post.getUpdatedOn())
                .build();
    }
}
