package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;

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
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }
}