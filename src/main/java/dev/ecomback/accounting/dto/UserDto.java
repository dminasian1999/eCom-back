package dev.ecomback.accounting.dto;

import lombok.*;

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
}
