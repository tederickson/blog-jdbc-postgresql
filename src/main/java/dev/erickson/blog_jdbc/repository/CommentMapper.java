package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Comment;
import dev.erickson.blog_jdbc.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class CommentMapper implements RowMapper<Comment> {
    private final PostRepository postRepository;

    @Override
    public Comment mapRow(ResultSet resultSet, int i) throws SQLException {
        var builder = Comment.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .content(resultSet.getString("content"))
                .publishedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("published_on")))
                .updatedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("updated_on")));

        Long id = resultSet.getLong("post");
        Post post = postRepository.findById(id).orElseThrow();

        builder.post(post);

        return builder.build();
    }
}
