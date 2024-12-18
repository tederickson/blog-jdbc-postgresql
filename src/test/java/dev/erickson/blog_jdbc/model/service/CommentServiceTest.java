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
    void setUp() throws SQLException {
        Author author = authorService.findById(2L).orElseThrow();

        final PostEntity postEntityCreate = PostEntity.builder()
                .authorId(author.id())
                .title(TITLE)
                .content("CommentServiceTest CommentServiceTest CommentServiceTest")
                .build();
        final long postId = postRepository.save(postEntityCreate);

        postEntity = postRepository.findById(postId).orElseThrow();

        comment = Comment.builder()
                .name("Friendly comment")
                .content("Praise, kudos")
                .postId(postId).build();
    }

    @Test
    void count() {
        assertEquals(0, commentService.count());
    }

    @Test
    void create() throws SQLException {
        assertEquals(0, commentService.count());
        Comment persistedComment = commentService.create(comment);

        assertNotNull(persistedComment);
        assertNotNull(persistedComment.id());
        assertEquals(comment.postId(), persistedComment.postId());
        assertNotNull(persistedComment.publishedOn());
        assertNull(comment.updatedOn());
        assertEquals(comment.content(), persistedComment.content());

        assertEquals(1, commentService.count());
    }

    @Test
    void findAll() throws SQLException {
        assertTrue(commentService.findAll().isEmpty());

        final int commentCount = 5;
        for (int i = 0; i < commentCount; i++) {commentService.create(comment);}

        assertEquals(commentCount, commentService.findAll().size());
        assertEquals(commentCount, commentService.count());
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
        assertEquals(persisted, comments.getFirst());
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

        Comment persistedComment = commentService.update(updated);
        assertNotNull(persistedComment);
        assertEquals(persisted.id(), persistedComment.id());
        assertEquals(persisted.postId(), persistedComment.postId());
        assertEquals("Troll troll troll", persistedComment.content());
        assertEquals("Rude comment", persistedComment.name());
        assertEquals(persisted.publishedOn(), persistedComment.publishedOn());
        assertNull(persisted.updatedOn());
        assertNotNull(persistedComment.updatedOn());
    }
}