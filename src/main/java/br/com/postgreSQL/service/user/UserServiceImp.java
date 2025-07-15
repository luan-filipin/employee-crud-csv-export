package br.com.postgreSQL.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.postgreSQL.dto.mapper.user.UserMapper;
import br.com.postgreSQL.dto.mapper.user.UserResponseMapper;
import br.com.postgreSQL.dto.user.UserDto;
import br.com.postgreSQL.exception.user.LoginAlreadyExistsException;
import br.com.postgreSQL.model.User;
import br.com.postgreSQL.repository.UserRepository;
import br.com.postgreSQL.service.validator.user.UserValidator;
import lombok.RequiredArgsConstructor;
import br.com.postgreSQL.dto.user.UserFindByLoginDto;

@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService{
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserResponseMapper userResponseMapper;
	private final PasswordEncoder passwordEncoder;
	private final UserValidator userValidator;
	
	@Override
	public void createUser(UserDto userDto) {
		if(userRepository.existsByLogin(userDto.login())) {
			throw new LoginAlreadyExistsException();
		}
		User user = userMapper.toEntity(userDto);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User salvedUser = userRepository.save(user);
	}

	@Override
	public UserFindByLoginDto findUserByLogin(String login) {
		userValidator.validateLoginExists(login);
		User user = userRepository.findByLogin(login);
		return userResponseMapper.toDto(user);
	}
	
	

}
