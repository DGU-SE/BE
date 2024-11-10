package com.twofullmoon.howmuchmarket.exception;

public class UserLocationNullException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    public UserLocationNullException(String message) {
        super(message);
    }
}