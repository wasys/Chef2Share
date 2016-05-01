package com.android.utils.lib.exception;

public class DomainException extends Exception {
	private static final long serialVersionUID = 7419997425845289500L;
	private String key;

	public DomainException(String message) {
		super(message);
		this.key = message;
	}

	public DomainException(String message, Throwable e) {
		super(message,e);
	}

	public String getKey() {
		return key;
	}
}