package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.model.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class PostEntityMapper implements RowMapper<PostEntity> {
    private final AuthorRepository authorRepository;
    private final CommentRepository commentRepository;

    @Override
    public PostEntity mapRow(ResultSet resultSet, int i) throws SQLException {
        PostEntity postEntity = PostEntity.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .content(resultSet.getString("content"))
                .publishedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("published_on")))
                .updatedOn(MapperUtil.getLocalDateTime(resultSet.getTimestamp("updated_on")))
                .build();

        Long authorId = resultSet.getLong("author");
        AuthorEntity authorEntity = authorRepository.findById(authorId).orElseThrow();

        postEntity.setAuthorEntity(authorEntity);
        postEntity.setCommentEntities(commentRepository.findByPost(postEntity));

        return postEntity;
    }
}
