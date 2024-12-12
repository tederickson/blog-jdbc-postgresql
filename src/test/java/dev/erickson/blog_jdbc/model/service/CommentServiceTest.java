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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private Comment comment;

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

        comment = Comment.builder()
                .name("Friendly comment")
                .content("Praise, kudos")
                .postId(postEntity.getId()).build();
    }

    @Test
    void count() throws SQLException {
        assertEquals(0, commentService.count());
    }

    @Test
    void create() throws SQLException {
        Comment persistedComment = commentService.create(comment);

        assertNotNull(persistedComment);
        assertNotNull(persistedComment.id());
        assertEquals(comment.postId(), persistedComment.postId());
        assertNotNull(persistedComment.publishedOn());
        assertNull(comment.updatedOn());
        assertEquals(comment.content(), persistedComment.content());
    }

    @Test
    void findAll() throws SQLException {
        assertTrue(commentService.findAll().isEmpty());

        final int commentCount = 5;
        for (int i = 0; i < commentCount; i++) {commentService.create(comment);}

        assertEquals(commentCount, commentService.findAll().size());
    }


    @Test
    void findById_notFound() {
        assertTrue(commentService.findById(-1L).isEmpty());
    }

    @Test
    void findById() throws SQLException {
        Comment persisted = commentService.create(comment);
        Comment located = commentService.findById(persisted.id()).orElseThrow();

        assertEquals(persisted, located);
    }

    @Test
    void findByPost() throws SQLException {
        Comment persisted = commentService.create(comment);
        var comments = commentService.findByPost(postEntity);

        assertEquals(1, comments.size());
        assertEquals(persisted, comments.get(0));
    }

    @Test
    void findByPost_nullPost() {
        assertThrows(IllegalArgumentException.class, () -> commentService.findByPost(null));
    }

    @Test
    void deleteById_notFound() {
        assertEquals(0, commentService.deleteById(-123L));
    }

    @Test
    void deleteById_nullId() {
        assertThrows(IllegalArgumentException.class, () -> commentService.deleteById(null));
    }

    @Test
    void deleteById() throws SQLException {
        Comment saved = commentService.create(comment);
        var id = saved.id();
        assertEquals(1, commentService.deleteById(id));
    }

    @Test
    void update() throws SQLException {
        Comment persisted = commentService.create(comment);
        Comment updated = persisted.toBuilder()
                .name("Rude comment")
                .content("Troll troll troll").build();

        assertEquals(updated, commentService.update(updated));
    }
}