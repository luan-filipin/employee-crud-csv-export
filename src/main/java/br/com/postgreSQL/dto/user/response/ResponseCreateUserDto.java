package br.com.postgreSQL.dto.user.response;

public record ResponseCreateUserDto(String message) {

	public static final String CREATE_WITH_SUCESS = "Usuario criado com sucesso!";
	
	public ResponseCreateUserDto() {
		this(CREATE_WITH_SUCESS);
	}
}
