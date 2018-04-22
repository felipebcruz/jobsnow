package br.com.jobsnow.database_client.params;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Join {
	public final String nomeDaTabelaDaEsquerda;
	public final String nomeDoCampoDaEsquerda;
	public final String nomeDaTabelaDaDireita;
	public final String nomeDoCampoDaDireita;

	public Join(String nomeDaTabelaDaEsquerda, String nomeDoCampoDaEsquerda, String nomeDaTabelaDaDireita, String nomeDoCampoDaDireita) {
		this.nomeDaTabelaDaEsquerda = nomeDaTabelaDaEsquerda;
		this.nomeDoCampoDaEsquerda = nomeDoCampoDaEsquerda;
		this.nomeDaTabelaDaDireita = nomeDaTabelaDaDireita;
		this.nomeDoCampoDaDireita = nomeDoCampoDaDireita;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomeDaTabelaDaDireita == null) ? 0 : nomeDaTabelaDaDireita.hashCode());
		result = prime * result + ((nomeDaTabelaDaEsquerda == null) ? 0 : nomeDaTabelaDaEsquerda.hashCode());
		result = prime * result + ((nomeDoCampoDaDireita == null) ? 0 : nomeDoCampoDaDireita.hashCode());
		result = prime * result + ((nomeDoCampoDaEsquerda == null) ? 0 : nomeDoCampoDaEsquerda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		Join join = (Join) obj;
		boolean naoEhMesmoTipo = false == obj instanceof Join;
		boolean naoEhMesmaTabelaDaEsquerda = false == this.nomeDaTabelaDaEsquerda.equalsIgnoreCase(join.nomeDaTabelaDaEsquerda);
		boolean naoEhMesmaTabelaDaDireita = false == this.nomeDaTabelaDaDireita.equalsIgnoreCase(join.nomeDaTabelaDaDireita);
		boolean naoEhMesmaCampoDaDireita = false == this.nomeDoCampoDaDireita.equalsIgnoreCase(join.nomeDoCampoDaDireita);
		boolean naoEhMesmaCampoDaEsquerda = false == this.nomeDoCampoDaEsquerda.equalsIgnoreCase(join.nomeDoCampoDaEsquerda);

		if (naoEhMesmoTipo) {
			return false;
		}

		if (naoEhMesmaTabelaDaEsquerda) {
			return false;
		}

		if (naoEhMesmaTabelaDaDireita) {
			return false;
		}

		if (naoEhMesmaCampoDaDireita) {
			return false;
		}

		if (naoEhMesmaCampoDaEsquerda) {
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