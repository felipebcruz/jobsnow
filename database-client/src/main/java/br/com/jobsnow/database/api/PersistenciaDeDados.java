package br.com.jobsnow.database.api;

import java.util.Collections;
import java.util.Set;

public class PersistenciaDeDados {

	public final String nomeDaTabela;
	
	public final Set<EspecificacaoCampo> camposComValores;

	public PersistenciaDeDados(String nomeDaTabela, Set<EspecificacaoCampo> camposComValores) {
		
		this.camposComValores = Collections.unmodifiableSet(camposComValores);
		this.nomeDaTabela = nomeDaTabela;
	}

	
	public AlteracaoDeDeRegistros _paraAlterar() {
		AlteracaoDeDeRegistros alteracaoDeDeRegistros = new AlteracaoDeDeRegistros( this.nomeDaTabela, this.camposComValores);
		return alteracaoDeDeRegistros;
	}
	
	
	public Long _paraInserirUmNovoRegistro() {
		//TODO 
//		DatabaseInterface dbi = new DatabaseInterface(this.nomeDaTabela);
//		
//		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = new LinkedHashSet<>();
//		
//		camposMaisSeusNovosValores.addAll(this.camposComValores);
//		
//		Long resultado = dbi._inserirUmUnicoRegistro(camposMaisSeusNovosValores);
//		
//		return resultado;

		
		return null;
	}
}
