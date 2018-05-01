package br.com.jobsnow.database.api;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import br.com.jobsnow.database.client.params.Sumarizacao;

public class CondicoesParaTrazerRegistros {

	public final Set<EspecificacaoCampo> criteriosParaSelecaoDosRegistros;
	
	public final String nomeDaTabela;
	

	public CondicoesParaTrazerRegistros(String nomeDaTabela, Set<EspecificacaoCampo> criteriosParaSelecaoDosRegistros) {
		this.criteriosParaSelecaoDosRegistros = criteriosParaSelecaoDosRegistros;
		this.nomeDaTabela = nomeDaTabela;
	}
	
	public boolean _verifiqueExistenciaDeRegistrosEmTaisCondicoes() {
		//TODO
		return false;
	}

	public Map<String, BigDecimal> _paraEntaoSumarizarValores(Sumarizacao ...sumarizacoes){
		//TODO
		return null;
	}
	
}
