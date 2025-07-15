package br.com.postgreSQL.dto.mapper.user;

import org.mapstruct.Mapper;

import br.com.postgreSQL.dto.user.UserFindByLoginDto;
import br.com.postgreSQL.model.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

	UserFindByLoginDto toDto(User user);
}
