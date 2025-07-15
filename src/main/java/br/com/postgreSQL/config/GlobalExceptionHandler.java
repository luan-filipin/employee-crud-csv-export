package br.com.postgreSQL.config;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.postgreSQL.dto.response.ErrorResponseDto;
import br.com.postgreSQL.dto.response.FieldErrorDto;
import br.com.postgreSQL.exception.employee.DocumentAlreadyExistsException;
import br.com.postgreSQL.exception.employee.DocumentImmutableException;
import br.com.postgreSQL.exception.employee.DocumentNotFoundException;
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
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request){
		List<FieldErrorDto> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
				.toList();
		
		ErrorResponseDto error = new ErrorResponseDto(
				"Erro na validação",
				HttpStatus.BAD_REQUEST.value(), 
				request.getRequestURI(),
				fieldErrors);
		return ResponseEntity.badRequest().body(error);
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
