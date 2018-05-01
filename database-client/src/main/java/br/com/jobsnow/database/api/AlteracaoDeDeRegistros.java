package br.com.jobsnow.database.api;

import java.util.Collections;
import java.util.Set;

public class AlteracaoDeDeRegistros {

	public Set<EspecificacaoCampo> camposComValores;
	public String nomeDaTabela;

	public AlteracaoDeDeRegistros(String nomeDaTabela, Set<EspecificacaoCampo> camposComValores) {
		this.camposComValores = Collections.unmodifiableSet(camposComValores);
		this.nomeDaTabela = nomeDaTabela;
	}

	
	public boolean _umUnicoRegistro(Long idDesteUnicoRegistro) {
		//TODO
		return false;
	}   
	
	
	public int _deUmConjuntoDeRegistrosQueAtendemDeterminadosCriteriosDeSelecao(Set<EspecificacaoCampo> criteriosParaSelecaoDosRegistros) {
		//TODO
		return 0;
	}
}
