package dev.ecomback.accounting.dto;

import dev.ecomback.accounting.model.Order;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	String firstName;
	String lastName;
	String login;
//	AddressDto address;
	@Singular
	Set<String> roles;
	AddressDto address;
	Map<String, Integer> cart;

	Set<String> wishList;

	List<Order> orders;
}
