package dev.ecomback.accounting.model;

import dev.ecomback.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"product"})
public class CartItem {

    Post product;
    Integer quantity;
}
