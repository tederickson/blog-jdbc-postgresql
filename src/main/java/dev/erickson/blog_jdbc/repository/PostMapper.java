package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Author;
import dev.erickson.blog_jdbc.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class PostMapper implements RowMapper<Post> {
    private final AuthorRepository authorRepository;

    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        Post post = Post.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .content(resultSet.getString("content"))
                .publishedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("published_on")))
                .updatedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("updated_on")))
                .build();

        Long authorId = resultSet.getLong("author");
        Author author = authorRepository.findById(authorId).orElseThrow();

        post.setAuthor(author);

        return post;
    }
}
