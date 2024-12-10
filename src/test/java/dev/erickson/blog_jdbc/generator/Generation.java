package dev.erickson.blog_jdbc.generator;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import dev.erickson.blog_jdbc.model.CommentEntity;
import org.junit.jupiter.api.Test;

public class Generation {

    @Test
    void author() throws NoSuchMethodException {
        CopyGenerator copyGenerator = new CopyGenerator();

        copyGenerator.toRestWithBuilder(AuthorEntity.class);
    }

    @Test
    void comment() throws NoSuchMethodException {
        CopyGenerator copyGenerator = new CopyGenerator();

        copyGenerator.toRestWithBuilder(CommentEntity.class);
    }
}
