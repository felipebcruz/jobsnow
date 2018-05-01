package br.com.jobsnow.database.api;

import java.util.List;
import java.util.Set;

public class Tabela {

	public final String nomeDaTabela;
	
	public Tabela(String nomeDaTabela) {
		this.nomeDaTabela = nomeDaTabela;
	}

	
	public PersistenciaDeDados _salveOsValores(Set<EspecificacaoCampo> camposComValores) {
		PersistenciaDeDados persistenciaDeDados = new PersistenciaDeDados(this.nomeDaTabela, camposComValores);
		return persistenciaDeDados;
	}
	
	public void _insiraVariosRegistrosDeUmaUnicaVez(List<Set<EspecificacaoCampo>> loteDeRegistros) {
		//TODO 

	}

	
	public AlteracaoDeStatus _altereStatus(Long novoValorDoStatus) {
		AlteracaoDeStatus alteracaoDeStatus = new AlteracaoDeStatus(this.nomeDaTabela, novoValorDoStatus);
		return alteracaoDeStatus;
	}
	
	
	public SelecaoDeRegistrosNumaUnicaTabela _facaUmaSelecaoDeRegistros() {
		SelecaoDeRegistrosNumaUnicaTabela selecaoDeRegistrosNumaUnicaTabela = new SelecaoDeRegistrosNumaUnicaTabela(this.nomeDaTabela);
		return selecaoDeRegistrosNumaUnicaTabela;
	}
	
	public SelecaoDeRegistrosEmMaisDeUmaTabela _adicioneEstaTabelaParaSelecaoDeRegistros(String nomeDaOutraTabela, String nomeDoCampoQueReferenciaEstaOutraTabela) {
		SelecaoDeRegistrosEmMaisDeUmaTabela selecaoDeRegistrosEmMaisDeUmaTabela = new SelecaoDeRegistrosEmMaisDeUmaTabela(this.nomeDaTabela, nomeDaOutraTabela, nomeDoCampoQueReferenciaEstaOutraTabela);
		return selecaoDeRegistrosEmMaisDeUmaTabela;
	}
	
}
