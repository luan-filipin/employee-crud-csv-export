package br.com.postgreSQL.exception.employee;

public class DocumentImmutableException extends RuntimeException{
	
	private static final String DOCUMENT_IMMUTABLE = "O document nao pode ser alterado!";
	
	public DocumentImmutableException() {
		super(DOCUMENT_IMMUTABLE);
	}

}
