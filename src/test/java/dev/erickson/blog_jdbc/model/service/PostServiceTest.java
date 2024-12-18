package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class PostServiceTest {
    private static final String TITLE = "My Test Post for PostServiceTest";
    @Autowired
    private AuthorService authorService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostService postService;

    private Author author;
    private Post post;

    @BeforeEach
    void setUp() {
        author = authorService.findById(3L).orElseThrow();

        post = Post.builder()
                .author(author)
                .title(TITLE)
                .content("Blah de blah blah")
                .build();
    }

    @Test
    void count() {
        assertEquals(0, postService.count());
    }

    @Test
    void findAll() {
        assertTrue(postService.findAll().isEmpty());
    }

    @Test
    void create() throws SQLException {
        Post persisted = postService.create(post);

        assertNotNull(persisted.getId());
        assertNull(post.getId());

        assertEquals(persisted.getAuthor(), post.getAuthor());
        assertEquals(persisted.getTitle(), post.getTitle());
        assertEquals(persisted.getContent(), post.getContent());

        assertTrue(CollectionUtils.isEmpty(persisted.getComments()));
        assertTrue(CollectionUtils.isEmpty(post.getComments()));

        assertNotNull(persisted.getPublishedOn());
        assertNull(post.getPublishedOn());

        assertNull(persisted.getUpdatedOn());
        assertNull(post.getUpdatedOn());

        assertEquals(1, postService.count());

        var posts = postService.findAll();
        assertEquals(1, posts.size());
        assertEquals(persisted, posts.getFirst());
    }

    @Test
    void create_withComments() throws SQLException {
        Comment comment = Comment.builder()
                .name("This is my name")
                .content("Nice succinct comment")
                .build();

        post.setComments(List.of(comment));
        Post persisted = postService.create(post);

        assertNotNull(persisted.getId());
        assertNull(post.getId());

        assertEquals(persisted.getAuthor(), post.getAuthor());
        assertEquals(persisted.getTitle(), post.getTitle());
        assertEquals(persisted.getContent(), post.getContent());

        assertEquals(post.getComments().size(), persisted.getComments().size());
        var persist1 = persisted.getComments().getFirst();

        assertNotNull(persist1.id());
        assertEquals(persisted.getId(), persist1.postId());
        assertEquals(comment.content(), persist1.content());
        assertEquals(comment.name(), persist1.name());
        assertNotNull(persist1.publishedOn());

        assertNotNull(persisted.getPublishedOn());
        assertNull(post.getPublishedOn());

        assertNull(persisted.getUpdatedOn());
        assertNull(post.getUpdatedOn());

        assertEquals(1, postService.count());

        var posts = postService.findAll();
        assertEquals(1, posts.size());
        assertEquals(persisted, posts.getFirst());
    }

    @Test
    void update_noComments() {
    }

    @Test
    void findById() throws SQLException {
        Post persisted = postService.create(post);

        assertEquals(persisted, postService.findById(persisted.getId()).orElseThrow());
    }

    @Test
    void findById_notFound() {
        assertTrue(postService.findById(-1L).isEmpty());
    }

    @Test
    void deleteById() throws SQLException {
        Post persisted = postService.create(post);
        assertEquals(1, postService.count());

        postService.deleteById(persisted.getId());
        assertEquals(0, postService.count());
    }

    @Test
    void deleteById_notFound() {
        postService.deleteById(-123L);
    }
}