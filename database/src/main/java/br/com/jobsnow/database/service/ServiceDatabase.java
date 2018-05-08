package br.com.jobsnow.database.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.jobsnow.database.params.DatabaseParamsDTO;

public interface ServiceDatabase {
	public List<Map<String, String>> _selecioneVariosRegistros(DatabaseParamsDTO params);
	
	public Map<String, String> _selecioneUmUnicoRegistro(DatabaseParamsDTO params);
	
	public Map<String, String> _obterTotais(DatabaseParamsDTO params);
	
	public boolean _verificarExistenciaDeRegistrosDadasEstasRestricoes(DatabaseParamsDTO params);
	
	public void _atualizarRegistros(DatabaseParamsDTO params);
	
	public Long _inserirUmUnicoRegistro(DatabaseParamsDTO params);
	
	public void _inserirRegistrosEmBatch(DatabaseParamsDTO params);

	public Set<String> _getCamposTabela(String tabela, String tblComNomesDosCampos);
}
