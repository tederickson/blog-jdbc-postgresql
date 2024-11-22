package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Comment;
import dev.erickson.blog_jdbc.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository implements DAO<Comment> {
    private final JdbcTemplate jdbcTemplate;
    private final CommentMapper commentMapper;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from comment", Integer.class);
    }

    public int save(Comment comment) {
        Assert.notNull(comment.getPost(), "missing Post");
        return jdbcTemplate.update(
                "insert into comment (post, name, content, published_on) values(?,?,?,?)",
                comment.getPost().getId(), comment.getName(), comment.getContent(), comment.getPublishedOn());
    }

    public int update(Comment comment) {
        Assert.notNull(comment.getPost(), "missing Post");
        return jdbcTemplate.update(
                "update comment set post=?, name=?, content=?, published_on=?, updated_on=? where id = ?",
                comment.getPost().getId(), comment.getName(), comment.getContent(), comment.getPublishedOn(),
                comment.getUpdatedOn(),
                comment.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from comment where id = ?",
                id);
    }

    public List<Comment> findAll() {
        return jdbcTemplate.query("select * from comment", commentMapper);
    }

    public Optional<Comment> findById(Long id) {
        return jdbcTemplate.query("select * from comment where id = ?", commentMapper, id)
                .stream()
                .findFirst();
    }

    public List<Comment> findByPost(Post post) {
        return jdbcTemplate.query("select * from comment where post = ?", commentMapper, post.getId());
    }
}
