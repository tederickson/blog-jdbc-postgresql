package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.model.CommentEntity;

public class CommentMapper {
    private CommentMapper() {
    }

    public static Comment toRest(final CommentEntity persistCommentEntity) {
        var builder = Comment.builder();
        return builder
                .id(persistCommentEntity.getId())
                .postId(persistCommentEntity.getPostId())
                .name(persistCommentEntity.getName())
                .content(persistCommentEntity.getContent())
                .publishedOn(persistCommentEntity.getPublishedOn())
                .updatedOn(persistCommentEntity.getUpdatedOn())
                .build();
    }

    public static CommentEntity toEntity(final Comment persistCommentEntity) {
        var builder = CommentEntity.builder();
        return builder
                .id(persistCommentEntity.id())
                .postId(persistCommentEntity.postId())
                .name(persistCommentEntity.name())
                .content(persistCommentEntity.content())
                .publishedOn(persistCommentEntity.publishedOn())
                .updatedOn(persistCommentEntity.updatedOn())
                .build();
    }
}
