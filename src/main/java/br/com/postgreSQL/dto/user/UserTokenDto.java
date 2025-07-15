package br.com.postgreSQL.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserTokenDto(
		@NotBlank(message = "O campo Login é obrigatorio")String login,
		@NotBlank(message = "O campo Password é obrigatoria")String password) {

}
