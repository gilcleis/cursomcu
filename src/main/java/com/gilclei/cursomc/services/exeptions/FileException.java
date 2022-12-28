package com.gilclei.cursomc.services.exeptions;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileException(String message) {
		super(message);
	}
	
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}
}