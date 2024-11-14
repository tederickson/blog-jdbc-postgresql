package dev.erickson.blog_jdbc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

@Data
public final class Author {
    @Id
    Integer id;

    String firstName;
    String lastName;
    String email;
    String username;

    @Version
    Integer version;
}
