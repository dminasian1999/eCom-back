package dev.ecomback.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewPostDto {
    private String name;
    private String imageUrl;
    private Integer quantity;
    private Double sell;
    private Double buy;
    private String category;


}
