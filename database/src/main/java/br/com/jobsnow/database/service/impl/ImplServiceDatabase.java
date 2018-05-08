package br.com.jobsnow.database.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.params.DatabaseParamsDTO.EntidadeSemTabelaException;
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
	public List<Map<String, String>> _selecioneVariosRegistros(DatabaseParamsDTO params) {
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		
		String sql = params._fazerSelect();
		
		List<Map<String, Object>> resultado = this.jdbcTemplate.queryForList(sql);
		
		List<Map<String, String>> _getListaFormatada = this._getListaFormatada(resultado);
		
		return _getListaFormatada; 
	}

	@Override
	public Map<String, String> _selecioneUmUnicoRegistro(DatabaseParamsDTO params) {
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		
		String sql = params._fazerSelectPeloId();
				
		Map<String, Object> resultado = this.jdbcTemplate.queryForMap(sql);
		
		Map<String, String> _getMapaFormatado = this._getMapaFormatado(resultado);
		
		return _getMapaFormatado;
	}

	@Override
	public Map<String, String> _obterTotais(DatabaseParamsDTO params){
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		
		String sql = params._fazerSelectDeAgregacao();
		
		Map<String, Object> resultado = this.jdbcTemplate.queryForMap(sql);

		Map<String, String> _getMapaFormatado = this._getMapaFormatado(resultado);
		return _getMapaFormatado;
	}

	@Override
	public boolean _verificarExistenciaDeRegistrosDadasEstasRestricoes(DatabaseParamsDTO params) {
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		
		String string = params._fazerSelectDeVerificacaoDeExistenciaDeRegistro();
		
		List<Map<String, Object>> resultado = this.jdbcTemplate.queryForList(string);
		
		int size = resultado.size();
		boolean b = size > 0;
		return b;
	}

	@Override
	public void _atualizarRegistros(DatabaseParamsDTO params){
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		
		boolean deveExecutarEmLotes = params.camposMaisSeusNovosValores.size() > 1;
		Map<String, Object> valores = new HashMap<>();
		
		if (false == deveExecutarEmLotes) {
			valores.put("idRegistro", params.idRegistro);
		}
		
		String sql = params._montarSqlDoUpdate();

		params.camposMaisSeusNovosValores.forEach(c -> valores.put(c.nomeDoCampo, c._converteParaTipoCorreto()));
		
		this.namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(valores));
	}
	
	@Override
	public Long _inserirUmUnicoRegistro(DatabaseParamsDTO params) {
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		
		String sql = params._montarSqlDoInsert();
		String colunaChavePrimaria = "id";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		Map<String, Object> _getValoresInsert = params._getValoresInsert();
		MapSqlParameterSource parametersInsert = new MapSqlParameterSource(_getValoresInsert);
		
		this.namedParameterJdbcTemplate.update(sql, parametersInsert, keyHolder, new String[]{colunaChavePrimaria});
		
		return keyHolder.getKey().longValue();
	}
	
	@SuppressWarnings("unchecked")
	public void _inserirRegistrosEmBatch(DatabaseParamsDTO params) {
		this._assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(params);
		String sql = params._montarSqlDoInsert();
		List<Map<String, Object>> batchValues = params._getValoresInsertBatch();
		
		this.namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[params.camposMaisSeusNovosValoresInsertBatch.size()]));
	}
	
	@Override
	public Set<String> _getCamposTabela(String tabela, String tblComNomesDosCampos) {
		String error = "Para fazer o select dos campos a tabela deve estar preenchida";

		if(tabela == null) {
			throw new EntidadeSemTabelaException(error);
		}
		
		boolean empty = tabela.trim().isEmpty();
		if(empty) {
			throw new EntidadeSemTabelaException(error);
		}
		
		StringBuilder sql = new StringBuilder("select column_name from ").append(tblComNomesDosCampos);
		sql.append(" where table_name='").append(tabela).append("'");
		
		String string = sql.toString();
		List<String> camposTabela = this.jdbcTemplate.queryForList(string, String.class);
		
		return new HashSet<>(camposTabela);
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
	
	private void _assegurarQueEstesParametrosNaoSaoNulosAndPossuemTabela(DatabaseParamsDTO params) {
		if(params == null) {
			throw new IllegalArgumentException("Os parametros do banco de dados nao foram informados");
		}
		
		params.assegurarQueEstaEntidadePossuiTabela();
	}
}