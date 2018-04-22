package br.com.jobsnow.database.params;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

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
	
	public boolean possuiOrdenacao() {
		if(this.camposParaOrdenacao == null) {
			return false;
		}
		if(this.camposParaOrdenacao.length <= 0) {
			return false;
		}
		return true;
	}
	
	
	public void assegurarQueCamposDeOrdenacaoNaoEstejamPresentes() {
		boolean possuiOrdenacao = this.possuiOrdenacao();
		if(possuiOrdenacao) {
			throw new CamposDeOrdenacaoNaoDeveriamExistir(this);
		}
	}
	
	@SuppressWarnings("serial")
	public static class CamposDeOrdenacaoNaoDeveriamExistir extends RuntimeException{
		private CamposDeOrdenacaoNaoDeveriamExistir(DatabaseParamsDTO dto) {
			super(getErrorMessage(dto));
		}

		private static String getErrorMessage(DatabaseParamsDTO dto) {
			String format = String.format("Os parametros '%s' nao deveriam conter ordenação", dto);
			return format;
		}
	}

	public void assegurarQueIdDaEntidadeEstaPresente() {
		if(this.idRegistro == null) {
			throw new IDDestaEntidadeDeveriaEstarPresente(this);
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class IDDestaEntidadeDeveriaEstarPresente extends RuntimeException{
		private IDDestaEntidadeDeveriaEstarPresente(DatabaseParamsDTO dto) {
			super(getErrorMessage(dto));
		}

		private static String getErrorMessage(DatabaseParamsDTO dto) {
			String format = String.format("Os parametros '%s' deveriam possuir o id da entidade", dto);
			return format;
		}
	}
	
	
	public void assegurarQueEstaEntidadePossuiTabela() {
		if(this.tabela == null ) {
			throw new EntidadeSemTabela(this);
		}

		boolean empty = this.tabela.trim().isEmpty();
		if(empty ) {
			throw new EntidadeSemTabela(this);
		}
	}
	
	
	@SuppressWarnings("serial")
	public static class EntidadeSemTabela extends RuntimeException{
		private EntidadeSemTabela(DatabaseParamsDTO dto) {
			super(getErrorMessage(dto));
		}

		private static String getErrorMessage(DatabaseParamsDTO dto) {
			String format = String.format("Os parametros '%s' deveriam possuir uma tabela", dto);
			return format;
		}
	}

	public boolean possuiAgrupamento() {
		
		if(this.camposParaAgrupamento == null) {
			return false;
		}
		
		if(this.camposParaAgrupamento.length <= 0) {
			return false;
		}
		
		return true;
	}

	public void assegurarQueEstaEntidadePossuiTantoCamposQuantoValores() {

		if(this.camposMaisSeusNovosValores == null) {
			throw new AusenciaDeCamposMaisValores(this);
		} 
		
		boolean empty = this.camposMaisSeusNovosValores.isEmpty();
		if(empty) {
			throw new AusenciaDeCamposMaisValores(this);
		}
	}
	
	@SuppressWarnings("serial")
	public static class AusenciaDeCamposMaisValores extends RuntimeException{
		private AusenciaDeCamposMaisValores(DatabaseParamsDTO dto) {
			super(getErrorMessage(dto));
		}

		private static String getErrorMessage(DatabaseParamsDTO dto) {
			String format = String.format("Os parametros '%s' deveriam possuir campos e valores", dto);
			return format;
		}
	}
	
	
	public String _montaAgregacoes() {
		boolean temCamposParaSelecionar = camposParaSelecionar != null && camposParaSelecionar.length > 0;
		boolean naoTemAgregacoes = this.funcoesAgregacao == null || this.funcoesAgregacao.isEmpty();
		
		if (naoTemAgregacoes) {
			// TODO
			throw new RuntimeException("");
		}
		
		String camposAgregacao = StringUtils.collectionToDelimitedString(this.funcoesAgregacao.stream().map(a -> a.nomeDaFuncao + "(" + a.nomeDoCampo + ")").collect(Collectors.toList()), ",");
		StringBuilder sql = new StringBuilder(camposAgregacao);
		
		if (temCamposParaSelecionar) {
			String camposSelect = StringUtils.arrayToCommaDelimitedString(camposParaSelecionar);
			sql.append(", ").append(camposSelect);
		}
		
		return sql.toString();
	}
	
	
	public String _fazerSelect() {
		
		
		String camposSelect = StringUtils.arrayToCommaDelimitedString(this.camposParaSelecionar);
		String camposOrdenacao = StringUtils.arrayToCommaDelimitedString(this.camposParaOrdenacao);
		StringBuilder sql = new StringBuilder("select ");

		sql.append(camposSelect).append(" from ").append(this.tabela).append(" \n");
		sql.append(this._montaJoins());
		sql.append(this._montaWhere());
		
		boolean temCamposParaOrdenar = this.possuiOrdenacao();
		if (temCamposParaOrdenar) {
			sql.append("order by ").append(camposOrdenacao);
		}
		
		return sql.toString();
	}

	public String _montaJoins() {
		boolean naoTemJoins = joins == null || joins.isEmpty();
		StringBuilder sql = new StringBuilder("\n");
		
		if (naoTemJoins) {
			return sql.toString();
		}
		
		joins.forEach(j -> {
			sql.append("left join ").append(j.nomeDaTabelaDaDireita).append(" \n");
			sql.append("on ").append(j.nomeDaTabelaDaEsquerda).append(".").append(j.nomeDoCampoDaEsquerda);
			sql.append(" = ");
			sql.append(j.nomeDaTabelaDaDireita).append(".").append(j.nomeDoCampoDaDireita);
			sql.append(" \n");
		});
		
		return sql.toString();
	}
	
	public String _montaWhere() {
		boolean naoTemRestricoes = restricoes == null || restricoes.isEmpty();
		StringBuilder sql = new StringBuilder("\n where 1=1 \n");

		if (naoTemRestricoes) {
			return sql.toString();
		}
		
		restricoes.forEach(r -> {
			sql.append("and ");
			sql.append(r.nomeDoCampo);
			sql.append(" = ");
			sql.append(r._converteParaTipoCorreto()); 
			sql.append(" \n");
		});
		
		return sql.toString();
	}
	
	public String _fazerSelectPeloId() {

		this.assegurarQueCamposDeOrdenacaoNaoEstejamPresentes();
		
		this.assegurarQueIdDaEntidadeEstaPresente();

		String _fazerSelect = this._fazerSelect();
		StringBuilder sql = new StringBuilder(_fazerSelect); 
		String tabela = this.tabela;
		String colunaChavePrimaria = "id";
		
		sql.append("and ").append(tabela).append(".").append(colunaChavePrimaria);
		sql.append(" = ").append(this.idRegistro);
		return sql.toString();
	}
	
	public String _fazerSelectDeAgregacao() {
		StringBuilder sql = new StringBuilder("select ");

		String _montaAgregacoes = this._montaAgregacoes();
		String _montaJoins = this._montaJoins();
		String _montaWhere = this._montaWhere();

		sql.append(_montaAgregacoes);
		sql.append(" from ").append(this.tabela);
		sql.append(_montaJoins);
		sql.append(_montaWhere);
		
		boolean possuiAgrupamento = this.possuiAgrupamento();
		
		if (possuiAgrupamento) {
			String camposAgrupamento = StringUtils.arrayToCommaDelimitedString(this.camposParaAgrupamento);
			sql.append("group by ").append(camposAgrupamento);
		}
		
		boolean possuiOrdenacao = this.possuiOrdenacao();
		if (possuiOrdenacao) {
			String camposOrdenacao = StringUtils.arrayToCommaDelimitedString(this.camposParaOrdenacao);
			sql.append("order by ").append(camposOrdenacao);
		}
		return sql.toString();
	}
	
	public String _fazerSelectDeVerificacaoDeExistenciaDeRegistro() {

		StringBuilder sql = new StringBuilder("select 1 from ").append(this.tabela).append(" \n");
		
		String _montaJoins = this._montaJoins();
		String _montaWhere = this._montaWhere();
		sql.append(_montaJoins);
		sql.append(_montaWhere);
		
		String string = sql.toString();
		return string;
	}
	
	public String _montarSqlDoUpdate() {

		this.assegurarQueEstaEntidadePossuiTantoCamposQuantoValores();
		
		this.assegurarQueIdDaEntidadeEstaPresente();
		
		String tabela = this.tabela;
		String colunaChavePrimaria = "id";
		StringBuilder sql = new StringBuilder("update ").append(tabela).append(" set ");
		
		this.camposMaisSeusNovosValores.forEach(c -> sql.append(c.nomeDoCampo).append(" = ").append(c._converteParaTipoCorreto()).append(","));
		sql.setLength(sql.length() - 1);

		sql.append(this._montaWhere());
		sql.append(" and ").append(tabela).append(".");
		sql.append(colunaChavePrimaria).append(" = ").append(this.idRegistro);
		
		return sql.toString();
	}
	
	public String _montarSqlDoInsert() {
		
		this.assegurarQueEstaEntidadePossuiTantoCamposQuantoValores();
		
		String tabela = this.tabela;
		String campos = StringUtils.collectionToDelimitedString(this.camposMaisSeusNovosValores.stream().map(c -> c.nomeDoCampo).collect(Collectors.toList()), ",");
		String valores = StringUtils.collectionToDelimitedString(this.camposMaisSeusNovosValores.stream().map(c -> ":"+c.nomeDoCampo).collect(Collectors.toList()), ",");
		StringBuilder sql = new StringBuilder("insert into ");

		sql.append(tabela).append("(").append(campos).append(") ");
		sql.append("VALUES (").append(valores).append(")");
		
		return sql.toString();
	}
	
	public Map<String, Object> _getValoresInsert(){

		Map<String, Object> valores = new HashMap<>();

		this.camposMaisSeusNovosValores.forEach(c -> valores.put(c.nomeDoCampo, c._converteParaTipoCorreto()));
		
		return valores;
	}


}