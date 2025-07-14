package br.com.postgreSQL.dto.response;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorResponseDto {

	private String message;
	private int status;
	private String path;
	private Instant timestamp;
	private List<FieldErrorDto> errors; 
	
	public ErrorResponseDto(String message, int status, String path) {
		this.message = message;
		this.status = status;
		this.path = path;
		this.timestamp = Instant.now();
	}
	
	// Esse construtor permiti que possamos usar o filedErrorDto, poderiamos apenas passar no construtor de cima, porem sempre que nao precisacemos seria necessario passar Null no campo.
	public ErrorResponseDto(String message, int status, String path, List<FieldErrorDto> errors) {
		this(message, status, path);
		this.errors = errors;
	}
}
