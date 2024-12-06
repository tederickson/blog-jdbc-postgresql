package dev.erickson.blog_jdbc.generator;

import dev.erickson.blog_jdbc.model.AuthorEntity;
import org.junit.jupiter.api.Test;

public class AuthorGeneration {

    @Test
    void toRestWithBuilder() throws NoSuchMethodException {
        CopyGenerator copyGenerator = new CopyGenerator();

        copyGenerator.toRestWithBuilder(AuthorEntity.class);
    }
}
