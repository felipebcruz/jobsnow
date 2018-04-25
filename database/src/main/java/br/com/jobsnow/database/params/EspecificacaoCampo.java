package br.com.jobsnow.database.params;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EspecificacaoCampo {
	public final String nomeDoCampo;
	public final String valorDoCampo;
	public final String tipoDoCampo;

	public EspecificacaoCampo() {
		this.nomeDoCampo = null;
		this.valorDoCampo = null;
		this.tipoDoCampo = null;
	}
	
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
	
	public Object _converteParaTipoCorreto() {
		//ver o pattern do date com o onias
		Gson g = new GsonBuilder().setDateFormat("dd/MM/YYYY HH:mm:ss").create();
		
		String objeto = g.toJson(this.valorDoCampo);
		Class<?> classe = this._getClassePorString();
		
		Object valor = g.fromJson(objeto, classe);

		return valor;
	}
	private Class<?> _getClassePorString(){
		Map<String, Class<?>> classes = new HashMap<>();
		classes.put("integer", Integer.class);
		classes.put("string", String.class);
		classes.put("date", Date.class);
		classes.put("long", Long.class);
		classes.put("double", Double.class);
		classes.put("string", String.class);
		
		return classes.get(this.tipoDoCampo.toLowerCase());
	}
	

}