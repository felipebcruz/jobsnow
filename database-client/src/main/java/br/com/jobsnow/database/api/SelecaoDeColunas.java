package br.com.jobsnow.database.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SelecaoDeColunas {

	public final String nomeDaTabela;
	public final Set<EspecificacaoCampo> quaisColunasDevemSerTrazidas;

	public SelecaoDeColunas(String nomeDaTabela, Set<EspecificacaoCampo> quaisColunasDevemSerTrazidas) {
		this.quaisColunasDevemSerTrazidas = quaisColunasDevemSerTrazidas;
		this.nomeDaTabela = nomeDaTabela;
	}

	
	public Map<String, String> _deUmUnicoRegistro(Long idDesteUnicoRegistro){
		// TODO
		return null;
	}

	public List<Map<String, String>> _deUmConjuntoDeRegistrosQueAtendemDeterminadosCriteriosDeSelecao(Set<EspecificacaoCampo> criteriosParaSelecaoDosRegistros, Ordenacao... ordenacoes){
		// TODO
		return null;
	}
	

}
