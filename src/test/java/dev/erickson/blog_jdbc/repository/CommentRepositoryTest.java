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
        Comment comment = Comment.builder()
                .post(post)
                .name(name)
                .content(content)
                .publishedOn(LocalDateTime.now())
                .build();
        assertEquals(1, commentRepository.save(comment));
        assertEquals(1, commentRepository.count());

        Comment dbComment = commentRepository.findByPost(post).get(0);
        assertEquals(name, dbComment.getName());
        assertEquals(content,dbComment.getContent());
        assertEquals(comment.getPublishedOn(), dbComment.getPublishedOn());
        assertNull(dbComment.getUpdatedOn());
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
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