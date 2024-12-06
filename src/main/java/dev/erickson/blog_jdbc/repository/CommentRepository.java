package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.CommentEntity;
import dev.erickson.blog_jdbc.model.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository implements DAO<CommentEntity> {
    private final JdbcTemplate jdbcTemplate;
    private final CommentEntityMapper commentEntityMapper;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from comment", Integer.class);
    }

    public int save(CommentEntity commentEntity) {
        Assert.notNull(commentEntity.getPostId(), "missing Post");
        return jdbcTemplate.update(
                "insert into comment (post_id, name, content, published_on) values(?,?,?,?)",
                commentEntity.getPostId() , commentEntity.getName(), commentEntity.getContent(), commentEntity.getPublishedOn());
    }

    public int update(CommentEntity commentEntity) {
        Assert.notNull(commentEntity.getPostId(), "missing Post");
        return jdbcTemplate.update(
                "update comment set post_id=?, name=?, content=?, published_on=?, updated_on=? where id = ?",
                commentEntity.getPostId() , commentEntity.getName(), commentEntity.getContent(), commentEntity.getPublishedOn(),
                commentEntity.getUpdatedOn(),
                commentEntity.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from comment where id = ?",
                id);
    }

    public List<CommentEntity> findAll() {
        return jdbcTemplate.query("select * from comment", commentEntityMapper);
    }

    public Optional<CommentEntity> findById(Long id) {
        return jdbcTemplate.query("select * from comment where id = ?", commentEntityMapper, id)
                .stream()
                .findFirst();
    }

    public List<CommentEntity> findByPost(PostEntity postEntity) {
        return jdbcTemplate.query("select * from comment where post_id = ?", commentEntityMapper, postEntity.getId());
    }
}
