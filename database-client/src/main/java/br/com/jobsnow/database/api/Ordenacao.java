package br.com.jobsnow.database.api;

public class Ordenacao {

	public final TipoOrdenacao tipoOrdenacao;
	
	public final String nomeDoCampo;
	
	
	private Ordenacao(TipoOrdenacao tipoOrdenacao, String nomeDoCampo) {
		super();
		this.tipoOrdenacao = tipoOrdenacao;
		this.nomeDoCampo = nomeDoCampo;
	}



	public static Ordenacao _ascendente(String nomeDoCampo) {
		Ordenacao ordenacao = new Ordenacao(TipoOrdenacao.ASC, nomeDoCampo);
		return ordenacao;
	}

	public static Ordenacao _descendente(String nomeDoCampo) {
		Ordenacao ordenacao = new Ordenacao(TipoOrdenacao.DESC, nomeDoCampo);
		return ordenacao;
	}

	private static enum TipoOrdenacao{
		ASC, DESC;
	}
}
