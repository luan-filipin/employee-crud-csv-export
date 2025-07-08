package br.com.postgreSQL.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.postgreSQL.dto.response.ErrorResponseDto;
import br.com.postgreSQL.exception.DocumentAlreadyExistsException;
import br.com.postgreSQL.exception.DocumentImmutableException;
import br.com.postgreSQL.exception.DocumentNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponseDto> handleGeneric(RuntimeException ex, HttpServletRequest request){
		ErrorResponseDto error = new ErrorResponseDto(
				ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> handleDocumentNotFound(DocumentNotFoundException ex, HttpServletRequest request){
		ErrorResponseDto error = new ErrorResponseDto(
				ex.getMessage(), 
				HttpStatus.NOT_FOUND.value(), 
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(DocumentAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> handleDocumentAlreadyExists(DocumentAlreadyExistsException ex, HttpServletRequest request) {
		ErrorResponseDto error = new ErrorResponseDto(
				ex.getMessage(),
				HttpStatus.CONFLICT.value(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
	
	@ExceptionHandler(DocumentImmutableException.class)
	public ResponseEntity<ErrorResponseDto> handleDocumentImmutable(DocumentImmutableException ex, HttpServletRequest request){
		ErrorResponseDto error = new ErrorResponseDto(
				ex.getMessage(),
				HttpStatus.BAD_REQUEST.value(),
				request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
