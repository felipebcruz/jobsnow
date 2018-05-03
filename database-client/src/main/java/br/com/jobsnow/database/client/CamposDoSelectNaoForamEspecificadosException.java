package br.com.jobsnow.database.client;

@SuppressWarnings("serial")
public class CamposDoSelectNaoForamEspecificadosException  extends RuntimeException{
	CamposDoSelectNaoForamEspecificadosException(){
		super("Especifique pelo menos um campo para trazer dados");
	}
}
