package br.com.jobsnow.database.api;

public class JobsNowDataBaseClient {

	public static Tabela _dentroDaTabela(String nomeDaTabela) {
		
		Tabela tabela = new Tabela(nomeDaTabela);
		return tabela;
		
	}
	
}
