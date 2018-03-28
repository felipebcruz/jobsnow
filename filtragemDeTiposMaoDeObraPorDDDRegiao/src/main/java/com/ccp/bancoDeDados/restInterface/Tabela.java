package com.ccp.bancoDeDados.restInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabela {

	public final List<String> campos;

	public Tabela(String nomeTabela) {

		this.campos = this.getCampos(nomeTabela);
	}

	private List<String> getCampos(String nomeTabela) {
		// TODO Fazer uma requisi��o rest ao banco de dados para saber quais campos tem nessa tabela
		return null;
	}
	
	public List<Map<String, String>> _selecioneVariosRegistros(Map<String, String> restricoes, String... quaisCamposTrazer){
		String[] nomes = new String[] {"011 - Tecnologia em Informatica","013 - Recursos Humanos","011 - Engenharia Civil","021 - Engenharia Eletrica","Administracao","Contabilidade"};
		
		int k = 0;
		List<Map<String, String>> resultado = new ArrayList<>();

		for (String nome : nomes) {
			
			Map<String, String> areaDeAtuacao = new HashMap<>();
			areaDeAtuacao.put("nome", nome);
			areaDeAtuacao.put("id", "" + k++);
			resultado.add(areaDeAtuacao);
			
			
		}
		return resultado;
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
