package dev.ecomback.accounting.model;

import dev.ecomback.accounting.dto.Cart;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;


@Getter
@Document(collection = "eCom-users")
public class UserAccount {
    @Id
    String login;
    @Setter
    String firstName;
    @Setter
    String lastName;
    @Setter
    String password;

    Set<Role> roles;
    @Setter
    Address address;
    Cart cart;

    Set<String> wishList;

    List<Order> orders;


    public UserAccount() {
        roles = new HashSet<>();
        wishList = new HashSet<>();
        orders = new ArrayList<>();
        cart = new Cart();

        addRole("USER");
//		this.createdAt = LocalDateTime.now();
//		this.updatedAt = LocalDateTime.now();
    }

    public UserAccount(String login, String password, String firstName, String lastName) {
        this();
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;


    }

    public boolean addRole(String role) {
        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(String role) {
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }

    public boolean addWishList(String productId) {

        return wishList.add(productId);
    }

    public boolean removeWishList(String productId) {

        return wishList.remove(productId);
    }
    public boolean addCartEntry(CartItem  cartItem) {
        return cart.addCartEntry(cartItem);

    }

    public boolean removeCartEntry(CartItem cartItem) {
        return cart.removeCartEntry(cartItem);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public boolean removeOrder(Order order) {
        return orders.remove(order);
    }

}
