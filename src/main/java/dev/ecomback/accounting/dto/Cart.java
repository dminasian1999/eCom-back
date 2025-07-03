package dev.ecomback.accounting.dto;

import dev.ecomback.accounting.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    String userId;
    Set<CartItem> items = new HashSet<>();
    Double totalPrice;



    public boolean addCartEntry(CartItem  cartItem) {
        return items. add(cartItem);

    }

    public boolean removeCartEntry(CartItem cartItem) {
        return items.remove(cartItem);
    }

}
