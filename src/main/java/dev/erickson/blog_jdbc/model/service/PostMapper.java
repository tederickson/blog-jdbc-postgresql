package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Post;
import dev.erickson.blog_jdbc.model.PostEntity;

public class PostMapper {
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

}
