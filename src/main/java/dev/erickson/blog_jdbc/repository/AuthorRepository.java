package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepository implements DAO<AuthorEntity> {
    private final JdbcTemplate jdbcTemplate;

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from author", Integer.class);
    }

    public int save(AuthorEntity authorEntity) {
        return jdbcTemplate.update(
                "insert into author (first_name, last_name, email, username) values(?,?,?,?)",
                authorEntity.getFirstName(), authorEntity.getLastName(), authorEntity.getEmail(), authorEntity.getUsername());
    }

    public int update(AuthorEntity authorEntity) {
        return jdbcTemplate.update(
                "update author set first_name=?, last_name=?, email=?, username=? where id = ?",
                authorEntity.getFirstName(), authorEntity.getLastName(), authorEntity.getEmail(), authorEntity.getUsername(), authorEntity.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update(
                "delete from author where id = ?",
                id);
    }

    public List<AuthorEntity> findAll() {
        return jdbcTemplate.query(
                "select * from author",
                new AuthorMapper()
        );
    }

    public Optional<AuthorEntity> findById(Long id) {
        return jdbcTemplate.query("select * from author where id = ?", new AuthorMapper(), id)
                .stream()
                .findFirst();
    }

    public Optional<AuthorEntity> findByEmail(String email) {
        return jdbcTemplate.query("select * from author where email = ?", new AuthorMapper(), email)
                .stream()
                .findFirst();
    }
}
