package dev.ecomback.post.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = {"author","dateCreated","message" })
public class Comment {
    @Setter
    String author;
    LocalDateTime dateCreated;
    @Setter
    String message;

    public Comment() {
        dateCreated = LocalDateTime.now();
    }

    public Comment(String author, String message) {
        this();
        this.author = author;
        this.message = message;
    }
}
