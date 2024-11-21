package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository implements DAO<Post> {
    private final JdbcTemplate jdbcTemplate;
    private final PostMapper postMapper;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from post", Integer.class);
    }

    public int save(Post post) {
        Assert.notNull(post.getAuthor(), "missing Author");
        return jdbcTemplate.update(
                "insert into post (title, content, published_on, author) values(?,?,?,?)",
                post.getTitle(), post.getContent(), post.getPublishedOn(),
                post.getAuthor().getId());
    }

    public int update(Post post) {
        Assert.notNull(post.getAuthor(), "missing Author");
        return jdbcTemplate.update(
                "update post set title=?, content=?, published_on=?, updated_on=?, author=? where id = ?",
                post.getTitle(), post.getContent(), post.getPublishedOn(), post.getUpdatedOn(),
                post.getAuthor().getId(), post.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from post where id = ?",
                id);
    }

    public List<Post> findAll() {
        return jdbcTemplate.query("select * from post", postMapper);
    }

    public Optional<Post> findById(Long id) {
        return jdbcTemplate.query("select * from post where id = ?", postMapper, id)
                .stream()
                .findFirst();
    }

    public List<Post> findByTitle(String title) {
        return jdbcTemplate.query("select * from post where title = ?", postMapper, title)
                .stream()
                .toList();
    }
}
