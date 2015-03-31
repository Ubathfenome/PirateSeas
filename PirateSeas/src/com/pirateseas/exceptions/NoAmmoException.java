package com.pirateseas.exceptions;

public class NoAmmoException extends Exception {
	
	private static final long serialVersionUID = -7086092562775180996L;

	public NoAmmoException(String message) {
		super(message);
	}
}
