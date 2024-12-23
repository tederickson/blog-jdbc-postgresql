package dev.erickson.blog_jdbc.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.domain.Comment;
import dev.erickson.blog_jdbc.domain.Post;
import dev.erickson.blog_jdbc.service.AuthorService;
import dev.erickson.blog_jdbc.service.CommentService;
import dev.erickson.blog_jdbc.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class PostServiceTest {
    private static final String TITLE = "My Test Post for PostServiceTest";
    @Autowired
    private AuthorService authorService;

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    private Post post;

    @BeforeEach
    void setUp() {
        Author author = authorService.findById(3L).orElseThrow();

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
        assertNull(persist1.updatedOn());

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
    void update_noComments() throws SQLException {
        Post persisted = postService.create(post);

        persisted.setAuthor(authorService.findById(1L).orElseThrow());
        persisted.setContent("This is a different author");
        persisted.setTitle("update_noComments");

        Post updated = postService.update(persisted);

        assertEquals(persisted.getId(), updated.getId());

        assertEquals(persisted.getAuthor(), updated.getAuthor());
        assertEquals(persisted.getTitle(), updated.getTitle());
        assertEquals(persisted.getContent(), updated.getContent());

        assertTrue(CollectionUtils.isEmpty(persisted.getComments()));
        assertTrue(CollectionUtils.isEmpty(updated.getComments()));

        assertEquals(persisted.getPublishedOn(), updated.getPublishedOn());

        assertNull(persisted.getUpdatedOn());
        assertNotNull(updated.getUpdatedOn());

        assertEquals(1, postService.count());
        assertEquals(0, commentService.count());
    }

    @Test
    void update_newComment() throws SQLException {
        assertEquals(0, commentService.count());

        Post persisted = postService.create(post);
        Comment comment = Comment.builder()
                .name("This is my name")
                .content("Nice succinct comment")
                .build();

        persisted.setComments(List.of(comment));

        Post updated = postService.update(persisted);

        assertEquals(persisted.getId(), updated.getId());
        assertEquals(persisted.getAuthor(), updated.getAuthor());
        assertEquals(persisted.getTitle(), updated.getTitle());
        assertEquals(persisted.getContent(), updated.getContent());
        assertEquals(persisted.getPublishedOn(), updated.getPublishedOn());

        assertNull(persisted.getUpdatedOn());
        assertNotNull(updated.getUpdatedOn());

        assertEquals(1, updated.getComments().size());
        var persist1 = updated.getComments().getFirst();

        assertNotNull(persist1.id());
        assertEquals(persisted.getId(), persist1.postId());
        assertEquals(comment.content(), persist1.content());
        assertEquals(comment.name(), persist1.name());
        assertNotNull(persist1.publishedOn());
        assertNull(persist1.updatedOn());

        assertEquals(1, postService.count());
        assertEquals(1, commentService.count());
    }

    @Test
    void update_withComments() throws SQLException {
        assertEquals(0, commentService.count());

        Comment comment1 = Comment.builder()
                .name("This is my name")
                .content("Nice succinct comment")
                .build();
        Comment comment2 = Comment.builder()
                .name("This is comment 2")
                .content("comment comment comment")
                .build();
        Comment comment3 = Comment.builder()
                .name("This is comment 3")
                .content("comment three comment")
                .build();
        post.setComments(List.of(comment1, comment2));

        Post persisted = postService.create(post);
        List<Comment> comments = new ArrayList<>(persisted.getComments());

        var secondComment = comments.remove(1);
        var updatedComment = secondComment.toBuilder()
                .content("My updated content")
                .name("My updated name")
                .build();

        comments.add(comment3);
        comments.add(updatedComment);
        persisted.setComments(comments);

        Post updated = postService.update(persisted);

        assertEquals(1, postService.count());
        assertEquals(3, commentService.count());

        assertEquals(persisted.getId(), updated.getId());
        assertEquals(persisted.getAuthor(), updated.getAuthor());
        assertEquals(persisted.getTitle(), updated.getTitle());
        assertEquals(persisted.getContent(), updated.getContent());
        assertEquals(persisted.getPublishedOn(), updated.getPublishedOn());

        assertNull(persisted.getUpdatedOn());
        assertNotNull(updated.getUpdatedOn());

        assertEquals(3, updated.getComments().size());

        for (Comment comment : updated.getComments()) {
            assertNotNull(comment.id());
            assertEquals(persisted.getId(), comment.postId());
            assertNotNull(comment.publishedOn());

            switch (comment.name()) {
                case "This is my name":
                    assertNotNull(comment.updatedOn());
                    assertEquals("Nice succinct comment", comment.content());
                    break;
                case "This is comment 3":
                    assertNull(comment.updatedOn());
                    assertEquals("comment three comment", comment.content());
                    break;
                case "My updated name":
                    assertNotNull(comment.updatedOn());
                    assertEquals("My updated content", comment.content());
                    break;

                default:
                    fail(comment.toString());
            }
        }
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