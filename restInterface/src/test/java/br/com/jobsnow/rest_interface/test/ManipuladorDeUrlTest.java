package br.com.jobsnow.rest_interface.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.jobsnow.rest_interface.ManipuladorDeUrl;

public class ManipuladorDeUrlTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void _deveRetornarAUrlSemAlteracaoSeOPathVariablesEstiverNullOuVazio() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		ManipuladorDeUrl manipuladorDeUrl = new ManipuladorDeUrl();
		
		String urlRetornada = manipuladorDeUrl._adicionaPathVariablesNaUrl(null, url);
		
		assertEquals(urlRetornada, url);

		urlRetornada = manipuladorDeUrl._adicionaPathVariablesNaUrl(new HashMap<>(), url);
		
		assertEquals(urlRetornada, url);
	}
	
	@Test
	public void _deveLancarNullPointerExceptionCasoAUrlEstejaNull() {
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");

		this.expectedEx.expect(NullPointerException.class);
		new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, null);
	}
	
	@Test
	public void _devLancarNullPointerExceptionSeAUrlEstiverNullEOsPathVariablesTambemEstiveremNullOuVazios() {
		this.expectedEx.expect(NullPointerException.class);
		new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(null, null);
	}
	
	@Test
	public void _deveRetornarStringVaziaSeAUrlEstiverVazia() {
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");

		String url = new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, "");
		
		assertEquals(url, "");
	}
	
	@Test
	public void _deveSubstituirCorretamenteOsPathVariableNaUrl() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro/2";
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");
		
		String urlRetornada = new ManipuladorDeUrl()._adicionaPathVariablesNaUrl(pathVariables, url);
		
		assertEquals(urlRetornada, urlEsperada);
	}

	@Test
	public void _deveRetornarAMesmaUrlSeOsQueryParametersNaoEstiveremPreenchidos() {
		StringBuilder url = new StringBuilder("localhost:8080/jobsnow/teste");
		String urlEsperada = "localhost:8080/jobsnow/teste";
		
		new ManipuladorDeUrl()._adicionaQueryParametersNaUrl(null, url);
		
		assertEquals(url.toString(), urlEsperada);
		
		new ManipuladorDeUrl()._adicionaQueryParametersNaUrl(new HashMap<>(), url);
		
		assertEquals(url.toString(), urlEsperada);
	}
	
	@Test
	public void _deveAdicionarOsQueryParametersNaUrl() {
		StringBuilder url = new StringBuilder("localhost:8080/jobsnow/teste");
		String urlEsperada = "localhost:8080/jobsnow/teste?numero=1&palavra=teste";
		Map<String, String> queryParameters = new LinkedHashMap<>();
		queryParameters.put("numero", "1");
		queryParameters.put("palavra", "teste");
		
		new ManipuladorDeUrl()._adicionaQueryParametersNaUrl(queryParameters, url);
		
		assertEquals(url.toString(), urlEsperada.toString());
	}
}