package com.gilclei.cursomc.services.exeptions;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message) {
		super(message);
	}
	
	public DatabaseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}