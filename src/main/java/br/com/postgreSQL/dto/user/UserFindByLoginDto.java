package br.com.postgreSQL.dto.user;

import br.com.postgreSQL.model.enums.UserRole;

public record UserFindByLoginDto(
		String login,
		UserRole userRole) {

}
