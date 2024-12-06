package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository implements DAO<PostEntity> {
    private final JdbcTemplate jdbcTemplate;
    private final PostMapper postMapper;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from post", Integer.class);
    }

    public int save(PostEntity postEntity) {
        Assert.notNull(postEntity.getAuthorEntity(), "missing Author");
        return jdbcTemplate.update(
                "insert into post (title, content, published_on, author) values(?,?,?,?)",
                postEntity.getTitle(), postEntity.getContent(), postEntity.getPublishedOn(),
                postEntity.getAuthorEntity().getId());
    }

    public int update(PostEntity postEntity) {
        Assert.notNull(postEntity.getAuthorEntity(), "missing Author");
        return jdbcTemplate.update(
                "update post set title=?, content=?, published_on=?, updated_on=?, author=? where id = ?",
                postEntity.getTitle(), postEntity.getContent(), postEntity.getPublishedOn(), postEntity.getUpdatedOn(),
                postEntity.getAuthorEntity().getId(), postEntity.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from post where id = ?",
                id);
    }

    public List<PostEntity> findAll() {
        return jdbcTemplate.query("select * from post", postMapper);
    }

    public Optional<PostEntity> findById(Long id) {
        return jdbcTemplate.query("select * from post where id = ?", postMapper, id)
                .stream()
                .findFirst();
    }

    public List<PostEntity> findByTitle(String title) {
        return jdbcTemplate.query("select * from post where title = ?", postMapper, title)
                .stream()
                .toList();
    }
}
