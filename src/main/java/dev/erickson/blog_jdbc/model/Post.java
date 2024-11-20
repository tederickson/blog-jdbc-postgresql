package dev.erickson.blog_jdbc.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Post {
    @Id
    private Long id;

    private Author author;
    private List<Comment> comments;

    private String title;
    private String content;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;
}
