package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Author;
import dev.erickson.blog_jdbc.model.Comment;
import dev.erickson.blog_jdbc.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class CommentRepositoryTest {
    public static final Long AUTHOR_ID = 5L;
    public static final String TITLE = "My Test Post";

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CommentRepository commentRepository;

    private Author author;

    private Post post;

    @BeforeEach
    void setUp() {
        author = authorRepository.findById(AUTHOR_ID).orElseThrow();

        postRepository.save(Post.builder()
                                    .author(author)
                                    .title(TITLE)
                                    .content("A Jules Version post")
                                    .publishedOn(LocalDateTime.now())
                                    .build());
        post = postRepository.findByTitle(TITLE).get(0);
    }

    @AfterEach
    void tearDown() {
        commentRepository.findByPost(post).forEach(row -> commentRepository.deleteById(row.getId()));
        postRepository.deleteById(post.getId());
    }

    @Test
    void count() {
        assertEquals(0, commentRepository.count());
    }

    @Test
    void save() {
        assertEquals(0, commentRepository.count());

        var name = "Friendly comment";
        var content = "Praise, kudos";
        saveComment(name, content);
        assertEquals(1, commentRepository.count());

        Comment dbComment = commentRepository.findByPost(post).get(0);
        assertEquals(name, dbComment.getName());
        assertEquals(content, dbComment.getContent());
        assertNotNull(dbComment.getPublishedOn());
        assertNull(dbComment.getUpdatedOn());
    }

    @Test
    void update() {
        var name = "Friendly comment";
        var content = "Praise, kudos";
        saveComment(name, content);

        Comment dbComment = commentRepository.findByPost(post).get(0);
        dbComment.setName("Troll comment");
        dbComment.setContent("rude, lies");
        dbComment.setUpdatedOn(LocalDateTime.now());

        assertEquals(1, commentRepository.update(dbComment));
        var comments = commentRepository.findByPost(post);

        assertEquals(1, comments.size());
        var updated = comments.get(0);

        assertNotNull(updated.getUpdatedOn());
        assertEquals(dbComment.getPost(), updated.getPost());
        assertEquals(dbComment.getName(), updated.getName());
        assertEquals(dbComment.getId(), updated.getId());
        assertEquals(dbComment.getContent(), updated.getContent());
        assertEquals(dbComment.getPublishedOn(), updated.getPublishedOn());
    }

    @Test
    void deleteById() {
        var name = "Friendly comment";
        var content = "Praise, kudos";
        saveComment(name, content);

        Comment dbComment = commentRepository.findByPost(post).get(0);

        assertEquals(1, commentRepository.deleteById(dbComment.getId()));
        assertEquals(0, commentRepository.count());
    }

    private void saveComment(String name, String content) {
        Comment comment = Comment.builder()
                .post(post)
                .name(name)
                .content(content)
                .publishedOn(LocalDateTime.now())
                .build();
        assertEquals(1, commentRepository.save(comment));
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
}