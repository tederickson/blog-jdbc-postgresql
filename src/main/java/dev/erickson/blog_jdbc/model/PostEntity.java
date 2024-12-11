package dev.erickson.blog_jdbc.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class PostEntity {
    @Id
    private Long id;

    private Long authorId;

    private String title;
    private String content;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;
}
