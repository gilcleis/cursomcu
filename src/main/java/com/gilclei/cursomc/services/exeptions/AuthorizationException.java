package com.gilclei.cursomc.services.exeptions;

public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthorizationException(String message) {
		super(message);
	}
	
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}