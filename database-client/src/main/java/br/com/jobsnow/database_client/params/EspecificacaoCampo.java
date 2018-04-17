package br.com.jobsnow.database_client.params;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EspecificacaoCampo {
	public final String nomeDoCampo;
	public final String valorDoCampo;
	public final String tipoDoCampo;

	public EspecificacaoCampo(String nomeDoCampo, String valorDoCampo, String tipoDoCampo) {
		this.nomeDoCampo = nomeDoCampo;
		this.valorDoCampo = valorDoCampo;
		this.tipoDoCampo = tipoDoCampo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeDoCampo == null) ? 0 : nomeDoCampo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		EspecificacaoCampo especificacaoCampo = (EspecificacaoCampo) obj;
		boolean naoEhMesmoTipo = false == obj instanceof EspecificacaoCampo;
		boolean naoEhMesmoNomeDoCampo = false == this.nomeDoCampo.equalsIgnoreCase(especificacaoCampo.nomeDoCampo);

		if (naoEhMesmoTipo) {
			return false;
		}

		if (naoEhMesmoNomeDoCampo) {
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