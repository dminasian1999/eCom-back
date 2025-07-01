package dev.ecomback.accounting.service;

import dev.ecomback.accounting.dto.AddressDto;
import dev.ecomback.accounting.dto.UserDto;
import dev.ecomback.accounting.dto.UserEditDto;
import dev.ecomback.accounting.dto.UserRegisterDto;

import java.util.List;

public interface UserAccountService {

	void changePassword(String login, String newPassword);

	void changeRolesList(String login, String role, boolean isAddRole);

	List<UserDto> getAllUsers();

	UserDto getUser(String login);

	void recoveryPassword(String token, String newPassword);
	void recoveryPasswordLink(String login);

	UserDto register(UserRegisterDto userRegisterDto);

	UserDto removeUser(String login);

	UserDto updateUser(String login, UserEditDto userEditDto);
	UserDto updateAddress(String login, AddressDto addressDto);

	UserDto changeWishList(String login, String productId, boolean isAdd);

	UserDto changeCartList(String login, String productId, boolean isAdd);

}
