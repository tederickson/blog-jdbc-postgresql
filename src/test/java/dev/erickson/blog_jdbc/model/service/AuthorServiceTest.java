package dev.erickson.blog_jdbc.model.service;

import dev.erickson.blog_jdbc.domain.Author;
import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthorServiceTest {
    private final static String email = "ted.erickson@invalid.org";
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
        Long id = authorRepository.findByEmail(email)
                .map(AuthorEntity::getId)
                .orElseThrow();
        authorRepository.deleteById(id);
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
}