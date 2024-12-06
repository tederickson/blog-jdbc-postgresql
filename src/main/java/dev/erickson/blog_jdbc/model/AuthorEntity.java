package dev.erickson.blog_jdbc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Data
@Builder
public final class AuthorEntity {
    @Id
    Long id;

    String firstName;
    String lastName;
    String email;
    String username;
}
