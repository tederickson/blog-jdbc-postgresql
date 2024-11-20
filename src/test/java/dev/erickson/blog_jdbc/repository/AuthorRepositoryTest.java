package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthorRepositoryTest {
    private final static String email = "ted.erickson@invalid.org";

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Author author;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("delete from author");

        author = Author.builder()
                .username("te0123")
                .firstName("Ted")
                .lastName("erickson")
                .email(email)
                .build();
    }

    @Test
    void count() {
        Integer count = authorRepository.count();
        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    void save() {
        Integer count = authorRepository.save(author);
        assertEquals(1, count);

        Author dbAuthor = authorRepository.findByEmail(email).orElseThrow();
        assertEquals(author.getUsername(), dbAuthor.getUsername());
        assertEquals(author.getFirstName(), dbAuthor.getFirstName());
        assertEquals(author.getLastName(), dbAuthor.getLastName());
        assertEquals(author.getEmail(), dbAuthor.getEmail());
    }

    @Test
    void update() {
        Integer count = authorRepository.save(author);
        assertEquals(1, count);

        Author dbAuthor = authorRepository.findByEmail(email).orElseThrow();
        dbAuthor.setUsername("yepUpdated");
        dbAuthor.setFirstName("modified");

        assertEquals(1, authorRepository.update(dbAuthor));

        dbAuthor = authorRepository.findByEmail(email).orElseThrow();
        assertEquals("yepUpdated", dbAuthor.getUsername());
        assertEquals("modified", dbAuthor.getFirstName());
        assertEquals(author.getLastName(), dbAuthor.getLastName());
        assertEquals(author.getEmail(), dbAuthor.getEmail());
    }

    @Test
    void deleteById() {
        assertEquals(1, authorRepository.save(author));

        Author dbAuthor = authorRepository.findByEmail(email).orElseThrow();

        Integer count = authorRepository.deleteById(dbAuthor.getId());
        assertNotNull(count);
        assertEquals(1, count);
    }

    @Test
    void deleteById_notFound() {
        Integer count = authorRepository.deleteById(-1L);
        assertNotNull(count);
        assertEquals(0, count);
    }

    @Test
    void findAll_noRows() {
        List<Author> authors = authorRepository.findAll();
        assertNotNull(authors);
        assertEquals(0, authors.size());
    }

    @Test
    void findAll() {
        authorRepository.save(author);

        List<Author> authors = authorRepository.findAll();
        assertNotNull(authors);
        assertEquals(1, authors.size());

        Author dbAuthor = authors.get(0);
        assertEquals(dbAuthor.getUsername(), author.getUsername());
        assertEquals(dbAuthor.getFirstName(), author.getFirstName());
        assertEquals(dbAuthor.getLastName(), author.getLastName());
        assertEquals(dbAuthor.getEmail(), author.getEmail());
    }

    @Test
    void findById() {
        Integer count = authorRepository.save(author);
        assertEquals(1, count);

        Author dbAuthor = authorRepository.findByEmail(email).orElseThrow();
        Author lookup = authorRepository.findById(dbAuthor.getId()).orElseThrow();

        assertEquals(dbAuthor, lookup);
    }

    @Test
    void findById_notFound() {
        assertTrue(authorRepository.findById(123L).isEmpty());
    }
}