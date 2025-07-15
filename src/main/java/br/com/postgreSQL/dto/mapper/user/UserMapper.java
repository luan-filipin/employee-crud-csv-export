package br.com.postgreSQL.dto.mapper.user;

import org.mapstruct.Mapper;

import br.com.postgreSQL.dto.user.UserDto;
import br.com.postgreSQL.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	UserDto toDto(User user);
	
	User toEntity(UserDto userDto);

}
