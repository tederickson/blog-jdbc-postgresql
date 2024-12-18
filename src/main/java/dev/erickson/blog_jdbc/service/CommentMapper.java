package dev.erickson.blog_jdbc.service;

import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.model.CommentEntity;

public class CommentMapper {
    private CommentMapper() {
    }

    public static Comment toRest(final CommentEntity commentEntity) {
        var builder = Comment.builder();
        return builder
                .id(commentEntity.getId())
                .postId(commentEntity.getPostId())
                .name(commentEntity.getName())
                .content(commentEntity.getContent())
                .publishedOn(commentEntity.getPublishedOn())
                .updatedOn(commentEntity.getUpdatedOn())
                .build();
    }

    public static CommentEntity toEntity(final Comment comment) {
        var builder = CommentEntity.builder();
        return builder
                .id(comment.id())
                .postId(comment.postId())
                .name(comment.name())
                .content(comment.content())
                .publishedOn(comment.publishedOn())
                .updatedOn(comment.updatedOn())
                .build();
    }
}
