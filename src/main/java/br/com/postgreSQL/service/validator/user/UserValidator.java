package br.com.postgreSQL.service.validator.user;

import org.springframework.stereotype.Component;

import br.com.postgreSQL.exception.user.LoginNotFoundException;
import br.com.postgreSQL.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {

	private final UserRepository userRepository;
	
	public void validateLoginExists(String login) {
		if(!userRepository.existsByLogin(login)) {
			throw new LoginNotFoundException();
		}
	}
	
	
}
