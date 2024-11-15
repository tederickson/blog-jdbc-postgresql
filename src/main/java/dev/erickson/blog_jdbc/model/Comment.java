package dev.erickson.blog_jdbc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

@Data
public final class Comment {
    @Id
    Integer id;

    @Transient
    Post post;

    @Version
    Integer version;

    private String name;
    private String content;
    private LocalDateTime publishedOn;
    private LocalDateTime updatedOn;

    public Comment(String name, String content) {
        this.name = name;
        this.content = content;
        this.publishedOn = LocalDateTime.now();
    }
}
