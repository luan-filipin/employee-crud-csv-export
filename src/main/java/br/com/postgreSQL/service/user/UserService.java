package br.com.postgreSQL.service.user;

import br.com.postgreSQL.dto.user.UserDto;
import br.com.postgreSQL.dto.user.UserFindByLoginDto;

public interface UserService {

	void createUser(UserDto userDto);
	
	UserFindByLoginDto findUserByLogin(String login);
}
