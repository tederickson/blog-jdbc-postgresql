package dev.erickson.blog_jdbc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Post {
    private final List<Comment> comments = new ArrayList<>();

    @Version
    Integer version;

    @Id
    private Integer id;

    private String title;
    private String content;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;

    public Post(String title, String content, LocalDateTime publishedOn) {
        this.title = title;
        this.content = content;
        this.publishedOn = publishedOn;
    }
}
