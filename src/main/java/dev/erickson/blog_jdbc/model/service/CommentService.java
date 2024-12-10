package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.model.CommentEntity;
import dev.erickson.blog_jdbc.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Integer count() {
        return commentRepository.count();
    }

    public Comment create(final Comment comment) throws SQLException {
        Assert.isNull(comment.id(), "The id must be null");
        Assert.notNull(comment.postId(), "The Post ID must not be null");

        CommentEntity commentEntity = CommentMapper.toEntity(comment);
        commentEntity.setPublishedOn(LocalDateTime.now());
        Long id = commentRepository.save(commentEntity);

        return CommentMapper.toRest(commentRepository.findById(id).orElseThrow());
    }

    public List<Comment> findAll() {
        return commentRepository.findAll().stream().map(CommentMapper::toRest).toList();
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id).map(CommentMapper::toRest);
    }

    public int deleteById(Long id) {
        return commentRepository.deleteById(id);
    }

    public Comment update(final Comment comment) {
        Assert.notNull(comment.id(), "The id must not be null");
        Assert.notNull(comment.postId(), "The Post ID must not be null");

        return commentRepository.findById(comment.id())
                .map(CommentMapper::toRest)
                .orElseThrow();
    }

}
