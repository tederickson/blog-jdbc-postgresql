package dev.erickson.blog_jdbc.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Comment(Long id,
                      Long postId,
                      String name,
                      String content,
                      LocalDateTime publishedOn,
                      LocalDateTime updatedOn) {
}
