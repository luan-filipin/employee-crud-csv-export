package br.com.postgreSQL.exception.employee;

public class DocumentAlreadyExistsException extends RuntimeException{

	private static final String DOCUMENT_ALREADY_EXISTS = "Este documento já existe!";
	
	public DocumentAlreadyExistsException() {
		super(DOCUMENT_ALREADY_EXISTS);
	}
}
