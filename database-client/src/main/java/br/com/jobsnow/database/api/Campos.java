package br.com.jobsnow.database.api;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Campos {

	public final Set<EspecificacaoCampo> especificacoes;
	
	private Campos(EspecificacaoCampo especificacaoCampo) {
		LinkedHashSet<EspecificacaoCampo> especificacoes = new LinkedHashSet<>();
		especificacoes.add(especificacaoCampo);
		this.especificacoes = Collections.unmodifiableSet(especificacoes);
	}
	
	private Campos(Campos outrasEspecificacoes, EspecificacaoCampo especificacaoCampo) {
		LinkedHashSet<EspecificacaoCampo> especificacoes = new LinkedHashSet<>();
		especificacoes.addAll(outrasEspecificacoes.especificacoes);
		especificacoes.add(especificacaoCampo);
		this.especificacoes = Collections.unmodifiableSet(especificacoes);
	}
	
	
	public static synchronized Campos _adicioneEsteCampo(EspecificacaoCampo especificacaoCampo) {
		Campos especificacoesDosCampos = new Campos(especificacaoCampo);
		return especificacoesDosCampos;
	}

	public static synchronized Campos _adicioneEsteCampoComEsteValor(String nomeDoCampo, String valorDoCampo) {
		EspecificacaoCampo especificacaoCampo = new EspecificacaoCampo(nomeDoCampo, valorDoCampo);
		Campos especificacoesDosCampos = new Campos(especificacaoCampo);
		return especificacoesDosCampos;
	}

	public static synchronized Campos _adicioneEsteCampo(String nomeDoCampo, String valorDoCampo, String tipoDoCampo) {
		EspecificacaoCampo especificacaoCampo = new EspecificacaoCampo(nomeDoCampo, valorDoCampo, tipoDoCampo);
		Campos especificacoesDosCampos = new Campos(especificacaoCampo);
		return especificacoesDosCampos;
	}
	
	public Campos _depoisAdicioneEsteCampo(EspecificacaoCampo especificacaoCampo) {
		Campos especificacoesDosCampos = new Campos(this, especificacaoCampo);
		return especificacoesDosCampos;
	}

	public Campos _depoisAdicioneEsteCampoComEsteValor(String nomeDoCampo, String valorDoCampo) {
		EspecificacaoCampo especificacaoCampo = new EspecificacaoCampo(nomeDoCampo, valorDoCampo);
		Campos especificacoesDosCampos = new Campos(especificacaoCampo);
		return especificacoesDosCampos;
	}

	public Campos _depoisAdicioneEsteCampo(String nomeDoCampo, String valorDoCampo, String tipoDoCampo) {
		EspecificacaoCampo especificacaoCampo = new EspecificacaoCampo(nomeDoCampo, valorDoCampo, tipoDoCampo);
		Campos especificacoesDosCampos = new Campos(especificacaoCampo);
		return especificacoesDosCampos;
	}

	
	
	
	public Set<EspecificacaoCampo> _porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados(){
		return this.especificacoes;
	}
}
