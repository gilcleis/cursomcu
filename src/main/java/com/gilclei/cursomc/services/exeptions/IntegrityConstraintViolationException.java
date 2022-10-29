package com.gilclei.cursomc.services.exeptions;

public class IntegrityConstraintViolationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public IntegrityConstraintViolationException(Object id) {
		super("Unique index or primary key violation "+id);
	}

}
