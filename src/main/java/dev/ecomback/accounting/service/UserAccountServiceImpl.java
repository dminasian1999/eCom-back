package dev.ecomback.accounting.service;

import dev.ecomback.accounting.dao.UserAccountRepository;
import dev.ecomback.accounting.dao.UserTokenRepository;
import dev.ecomback.accounting.dto.AddressDto;
import dev.ecomback.accounting.dto.UserDto;
import dev.ecomback.accounting.dto.UserEditDto;
import dev.ecomback.accounting.dto.UserRegisterDto;
import dev.ecomback.accounting.dto.exceptions.InvalidEmailExeption;
import dev.ecomback.accounting.dto.exceptions.TokenExpiredExeption;
import dev.ecomback.accounting.dto.exceptions.UserExistsException;
import dev.ecomback.accounting.dto.exceptions.UserNotFoundException;
import dev.ecomback.accounting.model.Address;
import dev.ecomback.accounting.model.UserAccount;
import dev.ecomback.accounting.model.UserToken;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {

	final ModelMapper modelMapper;
	final PasswordEncoder passwordEncoder;
	final UserAccountRepository userAccountRepository;
	final UserTokenRepository userTokenRepository;

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		userAccount.setPassword(passwordEncoder.encode(newPassword));
		userAccountRepository.save(userAccount);
	}

	@Override
	public void changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role);
		} else {
			res = userAccount.removeRole(role);
		}
		if (res) {
			userAccountRepository.save(userAccount);
		}
//		return modelMapper.map(userAccount, RolesDto.class);
	}

	private void checkEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new InvalidEmailExeption();
		}

	}
	@Override
	public List<UserDto> getAllUsers() {

//		return postRepository.findByAuthorIgnoreCase(publisherName).map(p -> modelMapper.map(p, PostDto.class))
	    return StreamSupport.stream(userAccountRepository.findAll().spliterator(), false)
	    		.map(u -> modelMapper.map(u, UserDto.class))
	    		.toList();
	}

	@Override
	public UserDto getUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public void recoveryPassword(String token, String newPassword) {
		UserToken userToken = userTokenRepository.findById(token).orElseThrow(TokenExpiredExeption::new);
		if (LocalDateTime.now().isAfter(userToken.getExpirationDate())) {
			userTokenRepository.delete(userToken);
			throw new TokenExpiredExeption();
		}
		changePassword(userToken.getLogin(), newPassword);
	}

	@Override
	public void recoveryPasswordLink(String login) {
		userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		UserToken userToken = new UserToken(login);
		userTokenRepository.save(userToken);
//		sendRecoveryEmail(login, userToken.getToken());

	}


	@Override
	public UserDto register(UserRegisterDto userRegisterDto) {
		if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsException();
		}
		checkEmail(userRegisterDto.getLogin());
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		userAccount.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto removeUser(String login) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		userAccountRepository.delete(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!userAccountRepository.existsById("admin")) {
			String password = passwordEncoder.encode("admin");
			UserAccount userAccount = new UserAccount("admin", password, "", "");
			userAccount.addRole("ADMINISTRATOR");
			userAccount.addRole("MODERATOR");
			userAccountRepository.save(userAccount);
		}
	}



	@Override
	public UserDto updateUser(String login, UserEditDto userEditDto) {
		UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
		if (userEditDto.getFirstName() != null) {
			userAccount.setFirstName(userEditDto.getFirstName());
		}
		if (userEditDto.getLastName() != null) {
			userAccount.setLastName(userEditDto.getLastName());
		}

		userAccountRepository.save(userAccount);
		return modelMapper.map(userAccount, UserDto.class);
	}

	@Override
	public UserDto updateAddress(String login, AddressDto addressDto) {
		UserAccount userAccount = userAccountRepository.findById(login)
				.orElseThrow(UserNotFoundException::new);

		Address address = new Address();

		if (addressDto.getFullName() != null) {
			address.setFullName(addressDto.getFullName());
		}
		if (addressDto.getStreet() != null) {
			address.setStreet(addressDto.getStreet());
		}
		if (addressDto.getCity() != null) {
			address.setCity(addressDto.getCity());
		}
		if (addressDto.getState() != null) {
			address.setState(addressDto.getState());
		}
		if (addressDto.getZipCode() != null) {
			address.setZipCode(addressDto.getZipCode());
		}
		if (addressDto.getCountry() != null) {
			address.setCountry(addressDto.getCountry());
		}
		if (addressDto.getPhone() != null) {
			address.setPhone(addressDto.getPhone());
		}

		userAccount.setAddress(address);
		userAccountRepository.save(userAccount);

		return modelMapper.map(userAccount, UserDto.class);
	}



}
