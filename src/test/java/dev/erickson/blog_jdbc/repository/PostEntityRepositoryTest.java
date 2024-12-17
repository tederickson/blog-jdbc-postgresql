package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.model.PostEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class PostEntityRepositoryTest {

    public static final String TITLE = "My Test Post";
    private final static String email = "ted.post@invalid.PostEntityRepositoryTest";

    @Autowired
    PostRepository postRepository;
    @Autowired
    AuthorRepository authorRepository;

    private PostEntity postEntity;

    @BeforeEach
    void setUp() {
        final AuthorEntity authorEntity = AuthorEntity.builder()
                .username("test_author")
                .firstName("Test")
                .lastName("Lastname")
                .email(email)
                .build();
        authorRepository.save(authorEntity);

        final AuthorEntity dbAuthorEntity = authorRepository.findByEmail(email).orElseThrow();

        postEntity = PostEntity.builder()
                .authorId(dbAuthorEntity.getId())
                .title(TITLE)
                .content("Blah de blah blah")
                .build();
    }

    @Test
    void count() {
        assertEquals(0, postRepository.count());
    }

    @Test
    void save() throws SQLException {
        var postId = postRepository.save(postEntity);
        assertEquals(1, postRepository.count());
        var possibleEntity = postRepository.findById(postId);

        assertTrue(possibleEntity.isPresent());
        var dbPost = possibleEntity.get();

        assertEquals(TITLE, dbPost.getTitle());
    }

    @Test
    void update() throws SQLException {
        final var postId = postRepository.save(postEntity);
        final var dbPost = postRepository.findById(postId).orElseThrow();

        assertNull(dbPost.getUpdatedOn());

        String content = "Generations of art";
        dbPost.setContent(content);

        assertEquals(1, postRepository.update(dbPost));

        final var updatedPost = postRepository.findById(postId).orElseThrow();
        assertEquals(content, updatedPost.getContent());

        assertNotNull(updatedPost.getUpdatedOn());
    }

    @Test
    void deleteById() throws SQLException {
        assertEquals(0, postRepository.deleteById(-1L));

        final var id = postRepository.save(postEntity);

        assertEquals(1, postRepository.deleteById(id));
        assertEquals(0, postRepository.count());
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