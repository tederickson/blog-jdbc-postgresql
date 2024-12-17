package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class PostRepository implements DAO<PostEntity> {
    private final JdbcTemplate jdbcTemplate;

    private static Long getKey(PreparedStatement statement) throws SQLException {
        // getLong(1) retrieves the first generated key from ResultSet. If the insert operation produces multiple
        // generated keys, we can access them using their respective positions.
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        }
        throw new SQLException("Unable to get keys");
    }

    public Integer count() {
        return jdbcTemplate
                .queryForObject("select count(*) from post", Integer.class);
    }

    public Long save(PostEntity postEntity) throws SQLException {
        Assert.notNull(postEntity.getAuthorId(), "missing Author");
        var sql = "insert into post (title, content, published_on, author_id) values(?,?,?,?)";

        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, postEntity.getTitle());
            statement.setString(2, postEntity.getContent());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(4, postEntity.getAuthorId());

            final int numRows = statement.executeUpdate();
            if (numRows != 1) {
                throw new SQLException("Unable to save " + postEntity);
            }

            return getKey(statement);
        }
    }

    public int update(PostEntity postEntity) {
        Assert.notNull(postEntity.getAuthorId(), "missing Author");
        return jdbcTemplate.update(
                "update post set title=?, content=?, published_on=?, updated_on=?, author_id=? where id = ?",
                postEntity.getTitle(), postEntity.getContent(), postEntity.getPublishedOn(),
                Timestamp.valueOf(LocalDateTime.now()),
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
