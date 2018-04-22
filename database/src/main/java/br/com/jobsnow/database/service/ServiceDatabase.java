package br.com.jobsnow.database.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.jobsnow.database.params.DatabaseParamsDTO;

public interface ServiceDatabase {
	public List<Map<String, String>> _selecioneVariosRegistros(DatabaseParamsDTO params) throws Exception;
	
	public Map<String, String> _selecioneUmUnicoRegistro(DatabaseParamsDTO params) throws Exception;
	
	public Map<String, String> _obterTotais(DatabaseParamsDTO params) throws Exception;
	
	public boolean _verificarExistenciaDeRegistrosDadasEstasRestricoes(DatabaseParamsDTO params) throws Exception;
	
	public void _atualizarUmUnicoRegistro(DatabaseParamsDTO params) throws Exception;
	
	public Long _inserirUmUnicoRegistro(DatabaseParamsDTO params) throws Exception;

	public Set<String> _getCamposTabela(String tabela);
}
