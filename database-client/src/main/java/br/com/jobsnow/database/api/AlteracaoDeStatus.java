package br.com.jobsnow.database.api;

import java.util.Set;

public class AlteracaoDeStatus {

	public final Long novoValorDoStatus;
	public final String nomeDaTabela;
	
	public AlteracaoDeStatus(String nomeDaTabela, Long novoValorDoStatus) {
		
		this.novoValorDoStatus = novoValorDoStatus;
		this.nomeDaTabela = nomeDaTabela;
	}

	
	public boolean _deUmUnicoRegistro(Long idDesteUnicoRegistro) {
		//TODO
		return false;
	}
	
	
	public int _deUmConjuntoDeRegistrosQueAtendemDeterminadosCriteriosDeSelecao(Set<EspecificacaoCampo> criteriosParaSelecaoDosRegistros) {
	//TODO
		return 0;
	}
}
