package br.com.postgreSQL.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto que representa a solicitação do token")
public record UserTokenDto(
		@Schema(description = "Login de acesso", example = "teste@gmail.com")
		@NotBlank(message = "O campo Login é obrigatorio")String login,
		@Schema(description = "Senhad e acesso", example = "123456789")
		@NotBlank(message = "O campo Password é obrigatoria")String password) {

}
