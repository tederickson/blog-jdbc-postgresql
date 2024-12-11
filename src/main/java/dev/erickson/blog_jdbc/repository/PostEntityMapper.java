package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.PostEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostEntityMapper implements RowMapper<PostEntity> {

    @Override
    public PostEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        return PostEntity.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .content(resultSet.getString("content"))
                .authorId(resultSet.getLong("author_id"))
                .publishedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("published_on")))
                .updatedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("updated_on")))
                .build();
    }
}
