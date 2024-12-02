package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Author;
import dev.erickson.blog_jdbc.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PostRepositoryTest {

    public static final String TITLE = "My Test Post";
    private final static String email = "ted.post@invalid.org";

    @Autowired
    PostRepository postRepository;
    @Autowired
    AuthorRepository authorRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        final Author author = Author.builder()
                .username("test_author")
                .firstName("Test")
                .lastName("Lastname")
                .email(email)
                .build();
        authorRepository.save(author);

        final Author dbAuthor = authorRepository.findByEmail(email).orElseThrow();

        post = Post.builder()
                .author(dbAuthor)
                .title(TITLE)
                .content("Blah de blah blah")
                .publishedOn(LocalDateTime.now())
                .comments(List.of())
                .build();
    }

    @AfterEach
    void tearDown() {
        List<Post> posts = postRepository.findByTitle(TITLE);

        posts.forEach(post -> postRepository.deleteById(post.getId()));

        var id = authorRepository.findByEmail(email).map(Author::getId).orElseThrow();
        assertEquals(1, authorRepository.deleteById(id));
    }

    @Test
    void count() {
        assertEquals(0, postRepository.count());
    }

    @Test
    void save() {
        assertEquals(1, postRepository.save(post));
    }

    @Test
    void update() {
        assertEquals(1, postRepository.save(post));
        final var dbPost = postRepository.findByTitle(TITLE).get(0);

        assertNull(dbPost.getUpdatedOn());

        post.setId(dbPost.getId());
        assertEquals(post, dbPost);

        String content = "Generations of art";
        dbPost.setContent(content);
        dbPost.setUpdatedOn(LocalDateTime.now());

        assertEquals(1, postRepository.update(dbPost));

        var updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(content, updatedPost.getContent());

        assertNotNull(updatedPost.getUpdatedOn());
    }

    @Test
    void deleteById() {
        assertEquals(0, postRepository.deleteById(-1L));

        postRepository.save(post);

        final var id = postRepository.findByTitle(TITLE).stream()
                .map(Post::getId)
                .findFirst()
                .orElseThrow();
        assertEquals(1, postRepository.deleteById(id));
    }

    @Test
    void findAll() {
        assertTrue(postRepository.findAll().isEmpty());
    }

    @Test
    void findById() {
        assertTrue(postRepository.findById(-1L).isEmpty());
    }

    @Test
    void findByTitle() {
        assertTrue(postRepository.findByTitle(TITLE).isEmpty());
    }
}