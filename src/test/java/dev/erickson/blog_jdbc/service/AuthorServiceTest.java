package dev.erickson.blog_jdbc.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.repository.AuthorRepository;
import dev.erickson.blog_jdbc.service.AuthorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class AuthorServiceTest {
    private final static String email = "ted.erickson@invalid.AuthorServiceTest";
    private final static int AUTHOR_COUNT = 6;

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .username("test_author")
                .firstName("Test")
                .lastName("Lastname")
                .email(email)
                .build();
    }

    @AfterEach
    void tearDown() {
        var id = authorRepository.findByEmail(email)
                .map(AuthorEntity::getId);
        id.ifPresent(aLong -> authorRepository.deleteById(aLong));
    }

    @Test
    void count() {
        Integer count = authorService.count();
        assertNotNull(count);
        assertEquals(AUTHOR_COUNT, count);
    }

    @Test
    void create() {
        Author persistedAuthor = authorService.create(author);

        assertNotNull(persistedAuthor.id());
        assertEquals(author.firstName(), persistedAuthor.firstName());
        assertEquals(author.lastName(), persistedAuthor.lastName());
        assertEquals(author.email(), persistedAuthor.email());
        assertEquals(author.username(), persistedAuthor.username());
    }

    @Test
    void create_invalid() {
        var exception = assertThrows(IllegalArgumentException.class,
                                     () -> authorService.create(Author.builder().id(123L).build()));
        assertEquals("The id must be null", exception.getMessage());
    }

    @Test
    void findAll() {
        List<Author> authors = authorService.findAll();
        assertNotNull(authors);
        assertEquals(AUTHOR_COUNT, authors.size());

        Author author1 = authors.getFirst();
        assertEquals("agatha", author1.username());
        assertEquals("Agatha", author1.firstName());
        assertEquals("Christie", author1.lastName());
        assertEquals("agatha@test.net", author1.email());
    }

    @Test
    void findById() {
        var persistedAuthor = authorService.create(author);
        var lookup = authorService.findById(persistedAuthor.id());

        assertTrue(lookup.isPresent());
        assertEquals(persistedAuthor, lookup.get());
    }

    @Test
    void deleteById_NotFound() {
        int count = authorService.deleteById(-1L);
        assertEquals(0, count);
    }

    @Test
    void deleteById() {
        Author persistedAuthor = authorService.create(author);
        int count = authorService.deleteById(persistedAuthor.id());
        assertEquals(1, count);
    }

    @Test
    void update() {
        Author author1 = authorService.create(author);
        Author updated = new Author(author1.id(), "Baba", "Yaga", email, "mythological");
        Author peristedAuthor = authorService.update(updated);

        assertEquals(authorService.findById(author1.id()).orElseThrow(), peristedAuthor);
    }

    @Test
    void update_invalid() {
        var exception = assertThrows(IllegalArgumentException.class,
                                     () -> authorService.update(Author.builder().build()));
        assertEquals("The id must not be null", exception.getMessage());
    }
}