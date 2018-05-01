package br.com.jobsnow.database.api;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class SelecaoDeRegistrosEmMaisDeUmaTabela {

	public final Set<Join> joins;
	
	public SelecaoDeRegistrosEmMaisDeUmaTabela(String nomeDaTabela, String nomeDaOutraTabela, 	String nomeDoCampoQueReferenciaEstaOutraTabela) {
		HashSet<Join> joins = new HashSet<>();
		Join join = new Join(nomeDaTabela, nomeDaOutraTabela, nomeDoCampoQueReferenciaEstaOutraTabela);
		joins.add(join);
		this.joins = Collections.unmodifiableSet(joins);
		
	}

	
	private SelecaoDeRegistrosEmMaisDeUmaTabela(SelecaoDeRegistrosEmMaisDeUmaTabela joinAnterior, String nomeDaTabela, String nomeDaOutraTabela, 	String nomeDoCampoQueReferenciaEstaOutraTabela) {
		
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		joins.addAll(joinAnterior.joins);
		Join join = new Join(nomeDaTabela, nomeDaOutraTabela, nomeDoCampoQueReferenciaEstaOutraTabela);
		joins.add(join);
		
		this.joins = Collections.unmodifiableSet(joins);
	}
	
	
	public SelecaoDeRegistrosEmMaisDeUmaTabela _depoisAdicioneEstaTabelaParaSelecaoDeRegistros(String nomeDaTabela, String nomeDaOutraTabela, 	String nomeDoCampoQueReferenciaEstaOutraTabela) {
		
		SelecaoDeRegistrosEmMaisDeUmaTabela selecaoDeRegistrosEmMaisDeUmaTabela = new SelecaoDeRegistrosEmMaisDeUmaTabela(this, nomeDaTabela, nomeDaOutraTabela, nomeDoCampoQueReferenciaEstaOutraTabela);
		return selecaoDeRegistrosEmMaisDeUmaTabela;
	}
	

}
