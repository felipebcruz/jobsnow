package br.com.jobsnow.rest_interface.exception;

@SuppressWarnings("serial")
public class UrlInvalidaException extends RuntimeException{

	public UrlInvalidaException(String error) {
		super(error);
	}
}