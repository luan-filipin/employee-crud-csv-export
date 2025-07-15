package br.com.postgreSQL.exception.user;

public class LoginAlreadyExistsException extends RuntimeException{
	
	private static final String LOGIN_ALREADY_EXISTS = "Esse login ja existe!";
	
	public LoginAlreadyExistsException() {
		super(LOGIN_ALREADY_EXISTS);
	}

}
