package br.com.jobsnow.rest_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.rest_interface.client.Client;
import br.com.jobsnow.rest_interface.exception.UrlInvalidaException;

public class JobsNowRequestInterface {
	public final String url;
	public final Client client;
	
	public JobsNowRequestInterface(String url) {
		this._assegurarQueAUrlEstaValida(url);
		this.url = url;
		this.client = new Client();
	}
	
	public JobsNowRequestInterface(String url, Client client) {
		this._assegurarQueAUrlEstaValida(url);
		this.url = url;
		this.client = client;
	}
	
	public List<Map<String, String>> _get(Map<String, String> pathVariables, Map<String, String> queryParameters){
		ManipuladorDeUrl manipuladorDeUrl = new ManipuladorDeUrl();
		
		String url = manipuladorDeUrl._adicionaPathVariablesNaUrl(pathVariables, this.url);
		
		StringBuilder urlFinal = new StringBuilder(url);
		manipuladorDeUrl._adicionaQueryParametersNaUrl(queryParameters, urlFinal);
		
		InputStream resposta = this.client._doGet(urlFinal.toString());
		
		List<Map<String, String>> retorno = null;
		String line = "";
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resposta);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<List<Map<String, String>>>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		return retorno;
	}

	public boolean _head(Map<String, String> pathVariables) {
		String url = new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, this.url);

		boolean resposta = this.client._doHead(url.toString());
		
		return resposta;
	}

	public void _delete(Map<String, String> pathVariables) {
		String url = new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, this.url);
		
		this.client._doDelete(url.toString());
	}
	
	public void _patch(Map<String, String> pathVariables,Map<String, String> corpoDeRequisicao) {
		String url = new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, this.url);
		
		this.client._doPatch(url.toString(), corpoDeRequisicao);
	}
	
	public Long _post(Map<String, String> pathVariables,Map<String, String> corpoDeRequisicao) {
		String url = new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, this.url);
		
		InputStream resposta = this.client._doPost(url.toString(), corpoDeRequisicao);

		Long idGerado = null;
		String line = "";
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resposta);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				idGerado = new ObjectMapper().readValue(line, new TypeReference<Long>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		return idGerado;
	}

	private void _assegurarQueAUrlEstaValida(String url) {
		boolean urlNula = url == null;
		
		if (urlNula) {
			throw new UrlInvalidaException("A url não pode ser null para instanciar o JobsNowRequestInterface");
		}
		
		boolean urlVazia = url.isEmpty();
		
		if (urlVazia) {
			throw new UrlInvalidaException("A url não pode estar vazia para instanciar JobsNowRequestInterface");
		}
	}
}