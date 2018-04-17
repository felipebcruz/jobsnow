package br.com.jobsnow.database_client.params;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FuncaoAgregacao {
	public final String nomeDaFuncao;
	public final String nomeDoCampo;
	
	public FuncaoAgregacao(String nomeFuncao, String nomeCampo) {
		this.nomeDaFuncao = nomeFuncao;
		this.nomeDoCampo = nomeCampo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeDoCampo == null) ? 0 : nomeDoCampo.hashCode());
		result = prime * result + ((nomeDaFuncao == null) ? 0 : nomeDaFuncao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		FuncaoAgregacao funcaoAgregacao = (FuncaoAgregacao) obj;

		boolean naoEhMesmoTipo = false == obj instanceof FuncaoAgregacao;
		boolean naoEhMesmoNomeDoCampo = false == this.nomeDoCampo.equalsIgnoreCase(funcaoAgregacao.nomeDoCampo);
		boolean naoEhMesmoNomeDaFuncao = false == this.nomeDaFuncao.equalsIgnoreCase(funcaoAgregacao.nomeDaFuncao);

		if (naoEhMesmoTipo) {
			return false;
		}

		if (naoEhMesmoNomeDoCampo) {
			return false;
		}
		
		if (naoEhMesmoNomeDaFuncao) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		GsonBuilder setPrettyPrinting = gsonBuilder.setPrettyPrinting();
		Gson create = setPrettyPrinting.create();
		String json = create.toJson(this);
		return json;
	}
}