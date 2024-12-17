package dev.erickson.blog_jdbc.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Post {
    private Long id;
    private Author author;
    private String title;
    private String content;
    private List<Comment> comments;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;
}
