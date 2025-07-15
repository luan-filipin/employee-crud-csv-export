package br.com.postgreSQL.exception.user;

public class LoginNotFoundException extends RuntimeException{
	
	private static final String LOGIN_NOT_FOUND = "Este login nao existe!";
	
	public LoginNotFoundException() {
		super(LOGIN_NOT_FOUND);
	}

}
