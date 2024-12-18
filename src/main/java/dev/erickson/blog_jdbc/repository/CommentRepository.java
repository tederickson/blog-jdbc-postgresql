package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.CommentEntity;
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
public class CommentRepository implements DAO<CommentEntity> {
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
        return jdbcTemplate.queryForObject("select count(*) from comment", Integer.class);
    }

    public Long save(CommentEntity commentEntity) throws SQLException {
        Assert.notNull(commentEntity.getPostId(), "missing Post");
        var sql = "insert into comment (post_id, name, content, published_on) values(?,?,?,?)";

        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, commentEntity.getPostId());
            statement.setString(2, commentEntity.getName());
            statement.setString(3, commentEntity.getContent());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            final int numRows = statement.executeUpdate();
            if (numRows != 1) {
                throw new SQLException("Unable to save " + commentEntity);
            }

            return getKey(statement);
        }
    }

    public int update(CommentEntity commentEntity) {
        Assert.notNull(commentEntity.getPostId(), "missing Post");
        return jdbcTemplate.update(
                "update comment set post_id=?, name=?, content=?, published_on=?, updated_on=? where id = ?",
                commentEntity.getPostId(), commentEntity.getName(), commentEntity.getContent(),
                commentEntity.getPublishedOn(),
                Timestamp.valueOf(LocalDateTime.now()),
                commentEntity.getId());
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from comment where id = ?", id);
    }

    public List<CommentEntity> findAll() {
        return jdbcTemplate.query("select * from comment", new CommentEntityMapper());
    }

    public Optional<CommentEntity> findById(Long id) {
        return jdbcTemplate.query("select * from comment where id = ?", new CommentEntityMapper(), id)
                .stream()
                .findFirst();
    }

    public List<CommentEntity> findByPost(PostEntity postEntity) {
        return jdbcTemplate.query("select * from comment where post_id = ?", new CommentEntityMapper(),
                                  postEntity.getId());
    }
}
