package com.douzone.mysite.exception;

public class BoardRepositoryException extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	public BoardRepositoryException(String message) {
		super(message);
	}
	
	public BoardRepositoryException() {
		super("BoardRepository 예외 발생");
	}
}
