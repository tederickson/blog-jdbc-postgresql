package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepository implements DAO<Author> {
    private final JdbcTemplate jdbcTemplate;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from author", Integer.class);
    }

    public int save(Author author) {
        return jdbcTemplate.update(
                "insert into author (first_name, last_name, email, username) values(?,?,?,?)",
                author.getFirstName(), author.getLastName(), author.getEmail(), author.getUsername());
    }

    public int update(Author author) {
        return jdbcTemplate.update(
                "update author set first_name=?, last_name=?, email=?, username=? where id = ?",
                author.getFirstName(), author.getLastName(), author.getEmail(), author.getUsername(), author.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from author where id = ?",
                id);
    }

    public List<Author> findAll() {
        return jdbcTemplate.query(
                "select * from author",
                new AuthorMapper()
        );
    }

    public Optional<Author> findById(Long id) {
        return jdbcTemplate.query("select * from author where id = ?", new AuthorMapper(), id)
                .stream()
                .findFirst();
    }

    public Optional<Author> findByEmail(String email) {
        return jdbcTemplate.query("select * from author where email = ?", new AuthorMapper(), email)
                .stream()
                .findFirst();
    }
}
