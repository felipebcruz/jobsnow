package br.com.jobsnow.database.client;

@SuppressWarnings("serial")
public class IdInvalidoException extends RuntimeException{
	IdInvalidoException(){
		super("Esta operação requer id de um campo, que por sua vez nao foi informado");
	}
}
