package dev.erickson.blog_jdbc.repository;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.model.CommentEntity;
import dev.erickson.blog_jdbc.model.PostEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CommentEntityRepositoryTest {
    public static final Long AUTHOR_ID = 5L;
    public static final String TITLE = "My Test Post For Comments";

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CommentRepository commentRepository;

    private PostEntity postEntity;

    @BeforeEach
    void setUp() {
        AuthorEntity authorEntity = authorRepository.findById(AUTHOR_ID).orElseThrow();

        postRepository.save(PostEntity.builder()
                                    .authorEntity(authorEntity)
                                    .title(TITLE)
                                    .content("A Jules Version post")
                                    .publishedOn(LocalDateTime.now())
                                    .build());
        postEntity = postRepository.findByTitle(TITLE).get(0);
    }

    @AfterEach
    void tearDown() {
        commentRepository.findByPost(postEntity).forEach(row -> commentRepository.deleteById(row.getId()));
        postRepository.deleteById(postEntity.getId());
    }

    @Test
    void count() {
        assertEquals(0, commentRepository.count());
    }

    @Test
    void save() throws SQLException {
        assertEquals(0, commentRepository.count());

        var name = "Friendly comment";
        var content = "Praise, kudos";
        saveComment(name, content);
        assertEquals(1, commentRepository.count());

        CommentEntity dbCommentEntity = commentRepository.findByPost(postEntity).get(0);
        assertEquals(name, dbCommentEntity.getName());
        assertEquals(content, dbCommentEntity.getContent());
        assertNotNull(dbCommentEntity.getPublishedOn());
        assertNull(dbCommentEntity.getUpdatedOn());
    }

    @Test
    void update() throws SQLException {
        var name = "Friendly comment";
        var content = "Praise, kudos";
        var commentId = saveComment(name, content);

        CommentEntity dbCommentEntity = commentRepository.findById(commentId).orElseThrow();
        dbCommentEntity.setName("Troll comment");
        dbCommentEntity.setContent("rude, lies");
        dbCommentEntity.setUpdatedOn(LocalDateTime.now());

        assertEquals(1, commentRepository.update(dbCommentEntity));
        var comments = commentRepository.findByPost(postEntity);

        assertEquals(1, comments.size());
        var updated = comments.get(0);

        assertNotNull(updated.getUpdatedOn());
        assertEquals(dbCommentEntity.getPostId(), updated.getPostId());
        assertEquals(dbCommentEntity.getName(), updated.getName());
        assertEquals(dbCommentEntity.getId(), updated.getId());
        assertEquals(dbCommentEntity.getContent(), updated.getContent());
        assertEquals(dbCommentEntity.getPublishedOn(), updated.getPublishedOn());
    }

    @Test
    void deleteById() throws SQLException {
        var name = "Friendly comment";
        var content = "Praise, kudos";
        var commentId = saveComment(name, content);

        CommentEntity dbCommentEntity = commentRepository.findByPost(postEntity).get(0);
        CommentEntity sameEntity = commentRepository.findById(commentId).orElseThrow();

        assertEquals(dbCommentEntity, sameEntity);

        assertEquals(1, commentRepository.deleteById(dbCommentEntity.getId()));
        assertEquals(0, commentRepository.count());
    }

    private Long saveComment(String name, String content) throws SQLException {
        CommentEntity commentEntity = CommentEntity.builder()
                .postId(postEntity.getId())
                .name(name)
                .content(content)
                .build();
        return commentRepository.save(commentEntity);
    }

    @Test
    void findAll() throws SQLException {
        var name = "Friendly comment";
        var content = "Praise, kudos";
        var numComments = 5;

        for (int i = 0; i < numComments; i++) {saveComment(name, content);}

        var comments = commentRepository.findAll();
        assertEquals(numComments, comments.size());

        comments.forEach(commentEntity -> {
            assertEquals(name, commentEntity.getName());
            assertEquals(content, commentEntity.getContent());
            assertNotNull(commentEntity.getPublishedOn());
            assertNull(commentEntity.getUpdatedOn());
        });
    }

    @Test
    void findById() throws SQLException {
        assertEquals(0, commentRepository.count());

        saveComment("Bob", "Bob critiques post");
        var comments = commentRepository.findAll();
        assertEquals(1, comments.size());

        var comment = commentRepository.findById(comments.get(0).getId()).orElseThrow();
        assertEquals(comments.get(0), comment);
    }

    @Test
    void findByPost() {
        assertTrue(commentRepository.findByPost(postEntity).isEmpty());
    }
}