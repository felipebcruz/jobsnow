package br.com.jobsnow.rest_interface;

import java.util.Map;

public class ManipuladorDeUrl {
	public String _adicionaPathVariablesNaUrl(Map<String, String> pathVariables, String url) {
		boolean pathVariablesInvalido = pathVariables == null || pathVariables.size() == 0;
		StringBuilder urlModificada = new StringBuilder();
		
		if (pathVariablesInvalido) {
			urlModificada.append(url);
			return url.toString();
		}
		
		String[] partesUrl = url.split("/");
		
		for (int i = 0; i < partesUrl.length; i++) {
			for (String variable : pathVariables.keySet()) {
				if(partesUrl[i].equals(variable)) {
					partesUrl[i] = pathVariables.get(variable);
				}
			}
			
			urlModificada.append(partesUrl[i]).append("/");
		}
		
		String urlFinal = urlModificada.substring(0, urlModificada.length() - 1);
		
		return urlFinal;
	}
	
	public void _adicionaQueryParametersNaUrl(Map<String, String> queryParameters, StringBuilder url) {
		boolean queryParametersInvalido = queryParameters == null || queryParameters.size() == 0;
		
		if (queryParametersInvalido) {
			return;
		}
		
		url.append("?");
		
		queryParameters.forEach((key,value) -> url.append(key).append("=").append(value).append("&"));
		
		url.setLength(url.length() - 1);
	}
}