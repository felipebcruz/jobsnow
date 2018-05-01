package br.com.jobsnow.database.api;

import java.util.Set;

public class SelecaoDeRegistrosNumaUnicaTabela {

	public final String nomeDaTabela;
	
	public SelecaoDeRegistrosNumaUnicaTabela(String nomeDaTabela) {
		this.nomeDaTabela = nomeDaTabela;
	}
	
	public SelecaoDeColunas _tragaEstasColunas(Set<EspecificacaoCampo> quaisColunasDevemSerTrazidas) {
		
		SelecaoDeColunas selecaoDeColunas = new SelecaoDeColunas(this.nomeDaTabela, quaisColunasDevemSerTrazidas);
		return selecaoDeColunas;
	}
	
	public CondicoesParaTrazerRegistros _comBaseNestesCriteriosDeSelecaoDeRegistros(Set<EspecificacaoCampo> criteriosParaSelecaoDosRegistros) {
		CondicoesParaTrazerRegistros condicoesParaTrazerRegistros = new CondicoesParaTrazerRegistros(this.nomeDaTabela, criteriosParaSelecaoDosRegistros);
		return condicoesParaTrazerRegistros;
	}
	
	
	

}
