package com.pirateseas.exceptions;

public class NotEnoughGoldException extends Exception {

	private static final long serialVersionUID = -2421229729590696484L;
	
	public NotEnoughGoldException(String message){
		super(message);
	}

}
