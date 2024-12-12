package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.model.PostEntity;
import dev.erickson.blog_jdbc.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class CommentServiceTest {
    public static final String TITLE = "CommentServiceTest";

    @Autowired
    private AuthorService authorService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;

    private PostEntity postEntity;

    @BeforeEach
    void setUp() {
        Author author = authorService.findById(2L).orElseThrow();

        final PostEntity postEntityCreate = PostEntity.builder()
                .authorId(author.id())
                .title(TITLE)
                .content("CommentServiceTest CommentServiceTest CommentServiceTest")
                .publishedOn(LocalDateTime.now())
                .build();
        assertEquals(1, postRepository.save(postEntityCreate));

        postEntity = postRepository.findByTitle(TITLE).get(0);
    }

    @Test
    void count() {
        assertEquals(0, commentService.count());
    }

    @Test
    void create() throws SQLException {
        var name = "Friendly comment";
        var content = "Praise, kudos";
        Comment comment = Comment.builder()
                .name(name)
                .content(content)
                .postId(postEntity.getId()).build();

        Comment persistedComment = commentService.create(comment);
        assertNotNull(persistedComment);
        assertNotNull(persistedComment.id());
        assertEquals(comment.postId(), persistedComment.postId());
        assertNotNull(persistedComment.publishedOn());
        assertNull(comment.updatedOn());
        assertEquals(comment.content(), persistedComment.content());
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByPost() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }
}