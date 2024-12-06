package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.CommentEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CommentEntityMapper implements RowMapper<CommentEntity> {
    @Override
    public CommentEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        return CommentEntity.builder()
                .id(resultSet.getLong("id"))
                .postId(resultSet.getLong("post_id"))
                .name(resultSet.getString("name"))
                .content(resultSet.getString("content"))
                .publishedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("published_on")))
                .updatedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("updated_on")))
                .build();
    }
}
