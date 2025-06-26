package dev.ecomback.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String id;
    private String name;
    private String imageUrl;
    private Integer quantity;
    private Double sell;
    private Double buy;
    private String category;
    private LocalDateTime dateCreated;
}
