package br.com.postgreSQL.dto.user;

import br.com.postgreSQL.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto que representa um usuario.")
public record UserDto(
		@Schema(description = "E-mail de acesso", example = "teste@gmail.com")
		@NotBlank(message = "O campo Login é obrigatorio")String login,
		@Schema(description = "Senha de acesso", example = "123456789")
		@NotBlank(message = "O campo Password é obrigatorio")String password,
		@Schema(description = "Tipo de acesso")
		@NotNull(message = "O campo Role é obrigatorio")UserRole userRole) {
}
