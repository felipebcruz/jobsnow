package br.com.jobsnow.database.params;

import java.util.LinkedHashSet;

public class DatabaseParamsDTO {
	public final Long idRegistro; 
	public final String tabela;
	public final String[] camposParaSelecionar;
	public final String[] camposParaOrdenacao;
	public final String[] camposParaAgrupamento;
	public final LinkedHashSet<FuncaoAgregacao> funcoesAgregacao;
	public final LinkedHashSet<Join> joins; 
	public final LinkedHashSet<EspecificacaoCampo> restricoes;
	public final LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores;
	
	public DatabaseParamsDTO() {
		this.camposParaSelecionar = null;
		this.tabela = null;
		this.joins = null;
		this.restricoes = null;
		this.camposParaOrdenacao = null;
		this.idRegistro = null;
		this.camposMaisSeusNovosValores = null;
		this.funcoesAgregacao = null;
		this.camposParaAgrupamento = null;
	}
	
	private DatabaseParamsDTO(String[] camposParaSelecionar, 
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
	
	public DatabaseParamsDTO(String[] camposParaSelecionar, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String[] camposParaOrdenacao) {
		this(camposParaSelecionar,tabela,joins, restricoes,null,camposParaOrdenacao,null,null,null);
	}

	public DatabaseParamsDTO(String[] camposParaSelecionar, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, Long idRegistro, String[] camposParaOrdenacao) {
		this(camposParaSelecionar,tabela,joins, restricoes,idRegistro,camposParaOrdenacao,null,null,null);
	}

	public DatabaseParamsDTO(String tabela, LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores,LinkedHashSet<EspecificacaoCampo> restricoes, Long idRegistro) {
		this(null,tabela,null, restricoes,idRegistro,null,camposMaisSeusNovosValores,null,null);
	}
	
	public DatabaseParamsDTO(String tabela, LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores) {
		this(null,tabela,null, null,null,null,camposMaisSeusNovosValores,null,null);
	}

	public DatabaseParamsDTO(LinkedHashSet<FuncaoAgregacao> funcoesAgregacao, String tabela, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaAgrupamento) {
		this(null,tabela,joins, restricoes,null,null,null,funcoesAgregacao,camposParaAgrupamento);
	}
}