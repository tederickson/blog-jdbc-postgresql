package dev.erickson.blog_jdbc.domain;

import lombok.Builder;

@Builder
public record Author(Long id,
                     String firstName,
                     String lastName,
                     String email,
                     String username) {
}
