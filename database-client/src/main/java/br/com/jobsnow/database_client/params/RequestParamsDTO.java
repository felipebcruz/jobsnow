package br.com.jobsnow.database_client.params;

import java.util.LinkedHashSet;

public class RequestParamsDTO {
	public final Long idRegistro; 
	public final String tabela;
	public final String[] camposParaSelecionar;
	public final String[] camposParaOrdenacao;
	public final String[] camposParaAgrupamento;
	public final LinkedHashSet<FuncaoAgregacao> funcoesAgregacao;
	public final LinkedHashSet<Join> joins; 
	public final LinkedHashSet<EspecificacaoCampo> restricoes;
	public final LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores;
	
	private RequestParamsDTO(String[] camposParaSelecionar, 
			String tabela, LinkedHashSet<Join> joins, 
			LinkedHashSet<EspecificacaoCampo> restricoes,
			Long idRegistro,
			String[] camposParaOrdenacao,
			LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores,
			LinkedHashSet<FuncaoAgregacao> funcoesAgregacao, 
			String[] camposParaAgrupamento) {
		
		this.camposParaSelecionar = camposParaSelecionar;
		this.tabela = tabela;
		this.joins = joins;
		this.restricoes = restricoes;
		this.idRegistro = idRegistro;
		this.camposParaOrdenacao = camposParaOrdenacao;
		this.camposMaisSeusNovosValores = camposMaisSeusNovosValores;
		this.funcoesAgregacao = funcoesAgregacao;
		this.camposParaAgrupamento = camposParaAgrupamento;
	}
	
	public RequestParamsDTO(String[] camposParaSelecionar, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String[] camposParaOrdenacao) {
		this(camposParaSelecionar,tabela,joins, restricoes,null,camposParaOrdenacao,null,null,null);
	}

	public RequestParamsDTO(String[] camposParaSelecionar, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, Long idRegistro, String[] camposParaOrdenacao) {
		this(camposParaSelecionar,tabela,joins, restricoes,idRegistro,camposParaOrdenacao,null,null,null);
	}

	public RequestParamsDTO(String tabela, LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores,LinkedHashSet<EspecificacaoCampo> restricoes, Long idRegistro) {
		this(null,tabela,null, restricoes,idRegistro,null,camposMaisSeusNovosValores,null,null);
	}
	
	public RequestParamsDTO(String tabela, LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores) {
		this(null,tabela,null, null,null,null,camposMaisSeusNovosValores,null,null);
	}

	public RequestParamsDTO(LinkedHashSet<FuncaoAgregacao> funcoesAgregacao, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaAgrupamento) {
		this(null,tabela,joins, restricoes,null,null,null,funcoesAgregacao,camposParaAgrupamento);
	}
}