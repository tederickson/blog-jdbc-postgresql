package dev.erickson.blog_jdbc.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@Data
public final class Comment {
    @Id
    private Long id;

    private Post post;

    private String name;
    private String content;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;
}
