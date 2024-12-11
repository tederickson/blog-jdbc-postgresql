package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Sql("/data/InitializeTests.sql")
class AuthorEntityRepositoryTest {
    private final static String email = "ted.erickson@invalid.AuthorEntityRepositoryTest";
    private final static int AUTHOR_COUNT = 6;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AuthorEntity authorEntity;

    @BeforeEach
    void setUp() {
        authorEntity = AuthorEntity.builder()
                .username("test_author")
                .firstName("Test")
                .lastName("Lastname")
                .email(email)
                .build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("delete from author where email = ?", email);
    }

    @Test
    void count() {
        Integer count = authorRepository.count();
        assertNotNull(count);
        assertEquals(AUTHOR_COUNT, count);
    }

    @Test
    void save() {
        Integer count = authorRepository.save(authorEntity);
        assertEquals(1, count);

        AuthorEntity dbAuthorEntity = authorRepository.findByEmail(email).orElseThrow();
        assertEquals(authorEntity.getUsername(), dbAuthorEntity.getUsername());
        assertEquals(authorEntity.getFirstName(), dbAuthorEntity.getFirstName());
        assertEquals(authorEntity.getLastName(), dbAuthorEntity.getLastName());
        assertEquals(authorEntity.getEmail(), dbAuthorEntity.getEmail());
    }

    @Test
    void update() {
        Integer count = authorRepository.save(authorEntity);
        assertEquals(1, count);

        AuthorEntity dbAuthorEntity = authorRepository.findByEmail(email).orElseThrow();
        dbAuthorEntity.setUsername("yepUpdated");
        dbAuthorEntity.setFirstName("modified");

        assertEquals(1, authorRepository.update(dbAuthorEntity));

        dbAuthorEntity = authorRepository.findByEmail(email).orElseThrow();
        assertEquals("yepUpdated", dbAuthorEntity.getUsername());
        assertEquals("modified", dbAuthorEntity.getFirstName());
        assertEquals(authorEntity.getLastName(), dbAuthorEntity.getLastName());
        assertEquals(authorEntity.getEmail(), dbAuthorEntity.getEmail());
    }

    @Test
    void deleteById() {
        assertEquals(1, authorRepository.save(authorEntity));

        AuthorEntity dbAuthorEntity = authorRepository.findByEmail(email).orElseThrow();

        Integer count = authorRepository.deleteById(dbAuthorEntity.getId());
        assertNotNull(count);
        assertEquals(1, count);

        assertEquals(AUTHOR_COUNT, authorRepository.count());
    }

    @Test
    void deleteById_notFound() {
        Integer count = authorRepository.deleteById(-1L);
        assertNotNull(count);
        assertEquals(0, count);
    }


    @Test
    void findAll() {
        List<AuthorEntity> authorEntities = authorRepository.findAll();
        assertNotNull(authorEntities);
        assertEquals(AUTHOR_COUNT, authorEntities.size());

        AuthorEntity dbAuthorEntity = authorEntities.get(0);
        assertEquals("agatha", dbAuthorEntity.getUsername());
        assertEquals("Agatha", dbAuthorEntity.getFirstName());
        assertEquals("Christie", dbAuthorEntity.getLastName());
        assertEquals("agatha@test.net", dbAuthorEntity.getEmail());
    }

    @Test
    void findById() {
        authorRepository.save(authorEntity);

        AuthorEntity dbAuthorEntity = authorRepository.findByEmail(email).orElseThrow();
        AuthorEntity lookup = authorRepository.findById(dbAuthorEntity.getId()).orElseThrow();

        assertEquals(dbAuthorEntity, lookup);
    }

    @Test
    void findById_notFound() {
        assertTrue(authorRepository.findById(123L).isEmpty());
    }
}