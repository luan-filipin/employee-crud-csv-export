package br.com.postgreSQL.dto.response;

import java.time.Instant;

import lombok.Data;

@Data
public class ErrorResponseDto {

	private String message;
	private int status;
	private String path;
	private Instant timestamp;
	
	public ErrorResponseDto(String message, int status, String path) {
		this.message = message;
		this.status = status;
		this.path = path;
		this.timestamp = Instant.now();
	}
}
