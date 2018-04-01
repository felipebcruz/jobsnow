package com.ccp.bancoDeDados.restInterface;

import java.util.List;
import java.util.Map;

public class Tabela {

	public final List<String> campos;

	public Tabela(String nomeTabela) {

		this.campos = this.getCampos(nomeTabela);
	}

	private List<String> getCampos(String nomeTabela) {
		// TODO Fazer uma requisição rest ao banco de dados para saber quais campos tem nessa tabela
		return null;
	}
	
	public List<Map<String, String>> _selecioneVariosRegistros(Map<String, String> restricoes, String... quaisCamposTrazer){
		return null;
	}
	
	public Map<String, String> _selecioneUmUnicoRegistro(Long idDesteRegistro,  String... quaisCamposTrazer){
		return null;
	}
	
	public Map<String, Integer> _obterTotais(Map<String, String> restricoes,  String... porQuaisCamposAgrupar){
		return null;
	}
	
	
	public Boolean _verificarExistenciaDeRegistrosDadasEstasRestricoes(Map<String, String> restricoes){
		return null;
	}
	
	public void _atualizarUmUnicoRegistro(Long idDesteRegistro,  Map<String, String> camposMaisSeusNovosValores){

	}
	
	public void _alterarStatusDeUmUnicoRegistro(Long idDesteRegistro,  Long valorDoStatus){

	}
	
	public Long _inserirUmUnicoRegistro(Map<String, String> camposMaisSeusNovosValores){
		return null;
	}
	
	
	
}
