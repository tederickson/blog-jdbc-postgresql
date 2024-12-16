package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class PostRepository implements DAO<PostEntity> {
    private final JdbcTemplate jdbcTemplate;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from post", Integer.class);
    }

    public int save(PostEntity postEntity) {
        Assert.notNull(postEntity.getAuthorId(), "missing Author");
        return jdbcTemplate.update(
                "insert into post (title, content, published_on, author_id) values(?,?,?,?)",
                postEntity.getTitle(), postEntity.getContent(), postEntity.getPublishedOn(),
                postEntity.getAuthorId());
    }

    public int update(PostEntity postEntity) {
        Assert.notNull(postEntity.getAuthorId(), "missing Author");
        return jdbcTemplate.update(
                "update post set title=?, content=?, published_on=?, updated_on=?, author_id=? where id = ?",
                postEntity.getTitle(), postEntity.getContent(), postEntity.getPublishedOn(), postEntity.getUpdatedOn(),
                postEntity.getAuthorId(), postEntity.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from post where id = ?",
                id);
    }

    public List<PostEntity> findAll() {
        return jdbcTemplate.query("select * from post", new PostEntityMapper());
    }

    public Optional<PostEntity> findById(Long id) {
        return jdbcTemplate.query("select * from post where id = ?", new PostEntityMapper(), id)
                .stream()
                .findFirst();
    }

    public List<PostEntity> findByTitle(String title) {
        return jdbcTemplate.query("select * from post where title = ?", new PostEntityMapper(), title)
                .stream()
                .toList();
    }
}
