package br.com.postgreSQL.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Retorno ao criar User.")
public record ResponseCreateUserDto(String message) {

	public static final String CREATE_WITH_SUCESS = "Usuario criado com sucesso!";
	
	public ResponseCreateUserDto() {
		this(CREATE_WITH_SUCESS);
	}
}
