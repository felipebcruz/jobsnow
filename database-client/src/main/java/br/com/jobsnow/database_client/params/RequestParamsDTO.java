package br.com.jobsnow.database_client.params;

import java.util.LinkedHashSet;

public class RequestParamsDTO {
	public final Long idRegistro; 
	public final String tabela;
	public final String[] camposParaSelecionar;
	public final String[] camposParaOrdenacao;
	public final LinkedHashSet<FuncaoAgregacao> funcaoAgregacao;
	public final LinkedHashSet<Join> joins; 
	public final LinkedHashSet<EspecificacaoCampo> restricoes;
	public final LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores;

	public RequestParamsDTO(String[] camposParaSelecionar, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String[] camposParaOrdenacao) {
		this.camposParaSelecionar = camposParaSelecionar;
		this.tabela = tabela;
		this.joins = joins;
		this.restricoes = restricoes;
		this.camposParaOrdenacao = camposParaOrdenacao;
		this.idRegistro = null;
		this.camposMaisSeusNovosValores = null;
		this.funcaoAgregacao = null;
	}
	
	public RequestParamsDTO(String[] camposParaSelecionar, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, Long idRegistro, String[] camposParaOrdenacao) {
		this.camposParaSelecionar = camposParaSelecionar;
		this.tabela = tabela;
		this.joins = joins;
		this.restricoes = restricoes;
		this.idRegistro = idRegistro;
		this.camposParaOrdenacao = camposParaOrdenacao;
		this.camposMaisSeusNovosValores = null;
		this.funcaoAgregacao = null;
	}

	public RequestParamsDTO(String tabela, LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores,LinkedHashSet<EspecificacaoCampo> restricoes, Long idRegistro) {
		this.tabela = tabela;
		this.camposMaisSeusNovosValores = camposMaisSeusNovosValores;
		this.restricoes = restricoes;
		this.idRegistro = idRegistro;
		this.joins = null;
		this.camposParaSelecionar = null;
		this.camposParaOrdenacao = null;
		this.funcaoAgregacao = null;
	}
	
	public RequestParamsDTO(String tabela, LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores) {
		this.tabela = tabela;
		this.camposMaisSeusNovosValores = camposMaisSeusNovosValores;
		this.idRegistro = null;
		this.camposParaSelecionar = null;
		this.joins = null;
		this.restricoes = null;
		this.camposParaOrdenacao = null;
		this.funcaoAgregacao = null;
	}

	public RequestParamsDTO(LinkedHashSet<FuncaoAgregacao> funcaoAgregacao, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaAgrupamento) {
		this.funcaoAgregacao = funcaoAgregacao;
		this.tabela = tabela;
		this.joins = joins;
		this.restricoes = restricoes;
		this.camposParaSelecionar = null;
		this.camposParaOrdenacao = null;
		this.idRegistro = null;
		this.camposMaisSeusNovosValores = null;
	}
}