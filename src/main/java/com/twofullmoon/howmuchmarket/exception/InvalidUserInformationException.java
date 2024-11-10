package com.twofullmoon.howmuchmarket.exception;

public class InvalidUserInformationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidUserInformationException(String message) {
        super(message);
    }
}
