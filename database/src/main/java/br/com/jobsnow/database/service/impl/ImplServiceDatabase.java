package br.com.jobsnow.database.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.params.EspecificacaoCampo;
import br.com.jobsnow.database.params.FuncaoAgregacao;
import br.com.jobsnow.database.params.Join;
import br.com.jobsnow.database.service.ServiceDatabase;

@Service
public class ImplServiceDatabase implements ServiceDatabase {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public ImplServiceDatabase(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public List<Map<String, String>> _selecioneVariosRegistros(DatabaseParamsDTO params) throws Exception {
		String sql = this._montaSql(params);
		List<Map<String, Object>> resultado = this.jdbcTemplate.queryForList(sql);
		
		return this._getListaFormatada(resultado); 
	}

	@Override
	public Map<String, String> _selecioneUmUnicoRegistro(DatabaseParamsDTO params) throws Exception {
		boolean temOrdenacao = params.camposParaOrdenacao != null && params.camposParaOrdenacao.length > 0;
		
		if (temOrdenacao) {
			throw new RuntimeException("Para selecionar um elemento não se deve preencher a ordenação");
		}
		
		if(params.idRegistro == null) {
			throw new IllegalArgumentException("O id do registro não pode ser null");
		}

		StringBuilder sql = new StringBuilder(this._montaSql(params)); 
		String tabela = params.tabela;
		String colunaChavePrimaria = this._getColunaPkFrom(tabela);
		
		sql.append("and ").append(tabela).append(".").append(colunaChavePrimaria);
		sql.append(" = ").append(params.idRegistro);
				
		Map<String, Object> resultado = this.jdbcTemplate.queryForMap(sql.toString());
		
		return this._getMapaFormatado(resultado);
	}
	
	@Override
	public Map<String, String> _obterTotais(DatabaseParamsDTO params) throws Exception{
		boolean temCamposParaAgrupar = params.camposParaAgrupamento != null && params.camposParaAgrupamento.length > 0;
		boolean temCamposParaOrdenar = params.camposParaOrdenacao != null && params.camposParaOrdenacao.length > 0;

		StringBuilder sql = new StringBuilder("select ");

		sql.append(this._montaAgregacoes(params.funcoesAgregacao, params.camposParaSelecionar));
		sql.append(" from ").append(params.tabela);
		sql.append(this._montaJoins(params.joins));
		sql.append(this._montaWhere(params.restricoes));
		
		if (temCamposParaAgrupar) {
			String camposAgrupamento = StringUtils.arrayToCommaDelimitedString(params.camposParaAgrupamento);
			sql.append("group by ").append(camposAgrupamento);
		}
		
		if (temCamposParaOrdenar) {
			String camposOrdenacao = StringUtils.arrayToCommaDelimitedString(params.camposParaOrdenacao);
			sql.append("order by ").append(camposOrdenacao);
		}
		
		Map<String, Object> resultado = this.jdbcTemplate.queryForMap(sql.toString());

		return this._getMapaFormatado(resultado);
	}

	@Override
	public boolean _verificarExistenciaDeRegistrosDadasEstasRestricoes(DatabaseParamsDTO params) throws Exception {
		boolean tabelaInvalida = params.tabela == null || params.tabela.isEmpty();
		
		if (tabelaInvalida) {
			// TODO
			throw new RuntimeException("");
		}
		
		StringBuilder sql = new StringBuilder("select 1 from ").append(params.tabela).append(" \n");
		
		sql.append(this._montaJoins(params.joins));
		sql.append(this._montaWhere(params.restricoes));
		
		List<Map<String, Object>> resultado = this.jdbcTemplate.queryForList(sql.toString());
		
		return resultado.size() > 0;
	}

	@Override
	public void _atualizarUmUnicoRegistro(DatabaseParamsDTO params) throws Exception{
		String sql = this._montaUpdate(params);
		this.jdbcTemplate.update(sql);
	}

	@Override
	public Long _inserirUmUnicoRegistro(DatabaseParamsDTO params) throws Exception {
		String sql = this._montaInsert(params);
		String colunaChavePrimaria = this._getColunaPkFrom(params.tabela);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource parametersInsert = new MapSqlParameterSource(this._getValoresInsert(params));
		
		this.namedParameterJdbcTemplate.update(sql, parametersInsert, keyHolder, new String[]{colunaChavePrimaria});
		
		return keyHolder.getKey().longValue();
	}
	
	@Override
	public Set<String> _getCamposTabela(String tabela) {
		boolean tabelaInvalida = tabela == null || tabela.isEmpty();
		
		if (tabelaInvalida) {
			throw new RuntimeException("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		}
		
		StringBuilder sql = new StringBuilder("select column_name from information_schema.columns ");
		sql.append("where table_name='").append(tabela).append("'");
		
		List<String> camposTabela = this.jdbcTemplate.queryForList(sql.toString(), String.class);
		
		return new HashSet<>(camposTabela);
	}
	
	private String _getColunaPkFrom(String tabela) {
		boolean tabelaInvalida = tabela == null || tabela.isEmpty();
		
		if (tabelaInvalida) {
			throw new RuntimeException("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		}
		
		StringBuilder sql = new StringBuilder("select kcu.COLUMN_NAME as coluna_pk ")
				.append(" from INFORMATION_SCHEMA.TABLE_CONSTRAINTS as tc ")
				.append(" join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as kcu ")
				.append(" on kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA ")
				.append(" and kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME ")
				.append(" and kcu.TABLE_SCHEMA = tc.TABLE_SCHEMA ")
				.append(" and kcu.TABLE_NAME = tc.TABLE_NAME ")
				.append(" where tc.CONSTRAINT_TYPE = 'PRIMARY KEY' ")
				.append(" and tc.TABLE_NAME = '").append(tabela).append("'");
		
		return this.jdbcTemplate.queryForObject(sql.toString(), String.class);
	}
	
	private Map<String, Object> _getValoresInsert(DatabaseParamsDTO params){
		Map<String, Object> valores = new HashMap<>();

		params.camposMaisSeusNovosValores.forEach(c -> valores.put(c.nomeDoCampo, this._converteParaTipoCorreto(c)));
		
		return valores;
	}
	
	private String _montaUpdate(DatabaseParamsDTO params) {
		if(params == null) {
			throw new NullPointerException("Para atualizar um registro os parametros devem estar preenchidos corretamente.");
		}
		
		boolean tabelaInvalida = params.tabela == null || params.tabela.isEmpty();
		
		if (tabelaInvalida) {
			throw new RuntimeException("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		}
		
		if(params.camposMaisSeusNovosValores == null) {
			throw new IllegalArgumentException("Os campos do update devem não podem estar null");
		}
		
		if (params.idRegistro == null) {
			throw new IllegalArgumentException("O id do registro a ser atualizado deve ser especificado.");
		}
		
		String tabela = params.tabela;
		String colunaChavePrimaria = this._getColunaPkFrom(tabela);
		StringBuilder sql = new StringBuilder("update ").append(tabela).append(" set ");
		
		params.camposMaisSeusNovosValores.forEach(c -> sql.append(c.nomeDoCampo).append(" = ").append(this._converteParaTipoCorreto(c)).append(","));
		sql.setLength(sql.length() - 1);

		sql.append(this._montaWhere(params.restricoes));
		sql.append(" and ").append(tabela).append(".");
		sql.append(colunaChavePrimaria).append(" = ").append(params.idRegistro);
		
		return sql.toString();
	}
	
	private String _montaInsert(DatabaseParamsDTO params) {
		if(params == null) {
			throw new NullPointerException("Para inserir um registro os parametros devem estar preenchidos corretamente.");
		}
		
		boolean tabelaInvalida = params.tabela == null || params.tabela.isEmpty();
		
		if (tabelaInvalida) {
			throw new RuntimeException("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		}
		
		if(params.camposMaisSeusNovosValores == null) {
			throw new IllegalArgumentException("O mapa com os valores a serem inseridos não podem estar null");
		}
		
		String tabela = params.tabela;
		String campos = StringUtils.collectionToDelimitedString(params.camposMaisSeusNovosValores.stream().map(c -> c.nomeDoCampo).collect(Collectors.toList()), ",");
		String valores = StringUtils.collectionToDelimitedString(params.camposMaisSeusNovosValores.stream().map(c -> ":"+c.nomeDoCampo).collect(Collectors.toList()), ",");
		StringBuilder sql = new StringBuilder("insert into ");

		sql.append(tabela).append("(").append(campos).append(") ");
		sql.append("VALUES (").append(valores).append(")");
		
		return sql.toString();
	}
	
	private String _montaSql(DatabaseParamsDTO params) {
		if(params == null) {
			throw new IllegalArgumentException("Os parametros devem estar preenchidos.");
		}
		
		boolean temCamposParaOrdenar = params.camposParaOrdenacao != null && params.camposParaOrdenacao.length > 0;
		boolean tabelaInvalida = params.tabela == null || params.tabela.isEmpty();
		
		if (tabelaInvalida) {
			throw new RuntimeException("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		}
		
		String camposSelect = StringUtils.arrayToCommaDelimitedString(params.camposParaSelecionar);
		String camposOrdenacao = StringUtils.arrayToCommaDelimitedString(params.camposParaOrdenacao);
		StringBuilder sql = new StringBuilder("select ");

		sql.append(camposSelect).append(" from ").append(params.tabela).append(" \n");
		sql.append(this._montaJoins(params.joins));
		sql.append(this._montaWhere(params.restricoes));
		
		if (temCamposParaOrdenar) {
			sql.append("order by ").append(camposOrdenacao);
		}
		
		return sql.toString();
	}
	
	private String _montaJoins(LinkedHashSet<Join> joins) {
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
	
	private String _montaWhere(LinkedHashSet<EspecificacaoCampo> restricoes) {
		boolean naoTemRestricoes = restricoes == null || restricoes.isEmpty();
		StringBuilder sql = new StringBuilder("\n where 1=1 \n");

		if (naoTemRestricoes) {
			return sql.toString();
		}
		
		restricoes.forEach(r -> {
			sql.append("and ");
			sql.append(r.nomeDoCampo);
			sql.append(" = ");
			sql.append(this._converteParaTipoCorreto(r)); 
			sql.append(" \n");
		});
		
		return sql.toString();
	}
	
	private String _montaAgregacoes(LinkedHashSet<FuncaoAgregacao> funcaoAgregacao, String[] camposParaSelecionar) {
		boolean naoTemAgregacoes = funcaoAgregacao == null || funcaoAgregacao.isEmpty();
		boolean temCamposParaSelecionar = camposParaSelecionar != null && camposParaSelecionar.length > 0;
		
		if (naoTemAgregacoes) {
			// TODO
			throw new RuntimeException("");
		}
		
		String camposAgregacao = StringUtils.collectionToDelimitedString(funcaoAgregacao.stream().map(a -> a.nomeDaFuncao + "(" + a.nomeDoCampo + ")").collect(Collectors.toList()), ",");
		StringBuilder sql = new StringBuilder(camposAgregacao);
		
		if (temCamposParaSelecionar) {
			String camposSelect = StringUtils.arrayToCommaDelimitedString(camposParaSelecionar);
			sql.append(", ").append(camposSelect);
		}
		
		return sql.toString();
	}
	
	private Object _converteParaTipoCorreto(EspecificacaoCampo esp) {
		//ver o pattern do date com o onias
		Gson g = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
		
		String objeto = g.toJson(esp.valorDoCampo);
		Class<?> classe = this._getClassePorString(esp.tipoDoCampo);
		
		Object valor = g.fromJson(objeto, classe);

		return valor;
	}
	
	private Map<String, String> _getMapaFormatado(Map<String, Object> resultado){
		if(resultado == null) {
			return null;
		}
		
		Map<String, String> mapaFormatado = new HashMap<>();
		
		resultado.forEach((k,v) -> mapaFormatado.put(k, String.valueOf(v)));
		
		return mapaFormatado;
	}
	
	private List<Map<String, String>> _getListaFormatada(List<Map<String, Object>> resultado){
		List<Map<String, String>> listaFormatada = new ArrayList<>();

		if(resultado == null) {
			return listaFormatada;
		}
		
		resultado.forEach(mapa -> {
			Map<String, String> mapaFormatado = new HashMap<>();
			mapa.forEach((k,v) -> mapaFormatado.put(k, String.valueOf(v)));
			listaFormatada.add(mapaFormatado);
		});
		
		return listaFormatada;
	}
	
	private Class<?> _getClassePorString(String classe){
		Map<String, Class<?>> classes = new HashMap<>();
		classes.put("integer", Integer.class);
		classes.put("string", String.class);
		classes.put("date", Date.class);
		classes.put("long", Long.class);
		classes.put("double", Double.class);
		
		return classes.get(classe.toLowerCase());
	}
}