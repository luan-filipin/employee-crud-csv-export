package br.com.postgreSQL.dto.user;

import br.com.postgreSQL.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
		@NotBlank(message = "O campo Login é obrigatorio")String login,
		@NotBlank(message = "O campo Password é obrigatorio")String password,
		@NotNull(message = "O campo Role é obrigatorio")UserRole userRole) {

}
