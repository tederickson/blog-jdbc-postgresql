package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.model.CommentEntity;
import dev.erickson.blog_jdbc.model.PostEntity;
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

    public void create(final Comment comment, final Long postId) throws SQLException {
        Assert.isNull(comment.id(), "The id must be null");
        Assert.notNull(postId, "The Post ID must not be null");

        CommentEntity commentEntity = CommentMapper.toEntity(comment);
        commentEntity.setPostId(postId);
        commentEntity.setPublishedOn(LocalDateTime.now());
        commentRepository.save(commentEntity);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll().stream().map(CommentMapper::toRest).toList();
    }

    public Optional<Comment> findById(Long id) {
        Assert.notNull(id, "The id must not be null");
        return commentRepository.findById(id).map(CommentMapper::toRest);
    }

    public List<Comment> findByPost(PostEntity postEntity) {
        Assert.notNull(postEntity, "The postEntity must not be null");
        return commentRepository.findByPost(postEntity).stream().map(CommentMapper::toRest).toList();
    }

    public int deleteById(Long id) {
        Assert.notNull(id, "The id must not be null");
        return commentRepository.deleteById(id);
    }

    public Comment update(final Comment comment) throws SQLException {
        Assert.notNull(comment.id(), "The id must not be null");
        Assert.notNull(comment.postId(), "The Post ID must not be null");

        CommentEntity entity = CommentMapper.toEntity(comment);
        int rowsChanged = commentRepository.update(entity);
        if (rowsChanged != 1) {throw new SQLException("Unable to update.  Rows changed = " + rowsChanged);}

        return commentRepository.findById(comment.id())
                .map(CommentMapper::toRest)
                .orElseThrow();
    }
}
