package com.ccp.jn;

import java.util.List;
import java.util.Map;
import java.util.Set;
//fazer o request para a string url da classe
public class JobsNowRequest {

	public final String url;

	public JobsNowRequest(String url) {
		this.url = url;
	}
	
	
	
	public List<Map<String, String>> _get(Map<String, String> pathVariables, Map<String, String> queryParameters){
		
		
		StringBuilder url = new StringBuilder(this.url);
		
		url.append("?");
		
		Set<String> keySet = queryParameters.keySet();
		
		for (String parameterName : keySet) {
			String parameterValue = queryParameters.get(parameterName);
			
			url.append(parameterName).append("=").append(parameterValue).append("&");
		}
		
		
		return null;
	}
	
	public boolean _head(Map<String, String> pathVariables) {
		return true;
	}
	

	public void _delete(Map<String, String> pathVariables) {
		
	}
	
	public void _patch(Map<String, String> pathVariables,Map<String, String> corpoDeRequisicao) {
		
	}
	public Long _post(Map<String, String> pathVariables,Map<String, String> corpoDeRequisicao) {
		return null;
	}
	
}
