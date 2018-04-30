package br.com.jobsnow.rest_interface.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.rest_interface.JobsNowRequestInterface;
import br.com.jobsnow.rest_interface.client.Client;
import br.com.jobsnow.rest_interface.exception.UrlInvalidaException;

public class JobsNowRequestInterfaceTest {
	private Client client;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void _setup() {
		this.client = mock(Client.class);
	}
	
	@Test
	public void _naoPodeInstanciarJobsNowRequestInterfaceComUrlVaziaOuNull() {
		this.expectedEx.expect(UrlInvalidaException.class);
		this.expectedEx.expectMessage("A url não pode ser null para instanciar o JobsNowRequestInterface");

		new JobsNowRequestInterface(null);
		
		this.expectedEx.expect(UrlInvalidaException.class);
		this.expectedEx.expectMessage("A url não pode estar vazia para instanciar JobsNowRequestInterface");
		
		new JobsNowRequestInterface("");
	}

	@Test
	public void _deveSubstituirCorretamenteOsPathVariableNaUrl() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro/2";
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");
		
	    when(this.client._doGet(urlEsperada))
    		.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode("").array()));
		
		new JobsNowRequestInterface(url, this.client)._get(pathVariables, null);
		
		verify(this.client, times(1))._doGet(urlEsperada);
		verifyNoMoreInteractions(this.client);
	}

	@Test
	public void _deveAdicionarOsQueryParametersNaUrl() {
		String url = "localhost:8080/jobsnow/teste";
		String urlEsperada = "localhost:8080/jobsnow/teste?numero=1&palavra=teste";
		Map<String, String> queryParameters = new LinkedHashMap<>();
		queryParameters.put("numero", "1");
		queryParameters.put("palavra", "teste");
		
	    when(this.client._doGet(urlEsperada))
    		.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode("").array()));
		
		new JobsNowRequestInterface(url, this.client)._get(null, queryParameters);
		
		verify(this.client, times(1))._doGet(urlEsperada);
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveSubstituirCorretamenteOsPathVariableEAdicionarOsQueryParametersNaUrl() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro/2?numero=3&palavra=teste";
		Map<String, String> pathVariables = new HashMap<>();
		Map<String, String> queryParameters = new LinkedHashMap<>();
		
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");
		queryParameters.put("numero", "3");
		queryParameters.put("palavra", "teste");
		
	    when(this.client._doGet(urlEsperada))
    		.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode("").array()));
		
		new JobsNowRequestInterface(url, this.client)._get(pathVariables, queryParameters);
		
		verify(this.client, times(1))._doGet(urlEsperada);
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveRetornarDadosDoGetComSucesso() throws JsonProcessingException {
		List<Map<String, String>> registrosEsperados = new ArrayList<Map<String, String>>();
		Map<String, String> ret = new HashMap<String, String>();
		Map<String, String> pathVariables = new HashMap<>();
		Map<String, String> queryParameters = new LinkedHashMap<>();
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro/2?numero=3&palavra=teste";

		ret.put("id_teste", "2");
		ret.put("teste", "2");
		registrosEsperados.add(ret);
		
		ret = new HashMap<String, String>();
		ret.put("id_teste", "3");
		ret.put("teste", "10");
		registrosEsperados.add(ret);
		
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");
		queryParameters.put("numero", "3");
		queryParameters.put("palavra", "teste");
		
		String conteudo = new ObjectMapper().writeValueAsString(registrosEsperados);
	    when(this.client._doGet(urlEsperada))
    		.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
		
		List<Map<String, String>> registros = new JobsNowRequestInterface(url, this.client)._get(pathVariables, queryParameters);
		
		assertEquals(registrosEsperados, registros);
		verify(this.client, times(1))._doGet(urlEsperada);
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveRetornarTrueNoHead() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro/2";
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");
		
	    when(this.client._doHead(urlEsperada)).thenReturn(true);
		
		boolean resposta = new JobsNowRequestInterface(url, this.client)._head(pathVariables);
		
		assertTrue(resposta);
		verify(this.client, times(1))._doHead(urlEsperada);
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveExecutarODelete() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro/{numero}";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro/2";
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("{id}", "1");
		pathVariables.put("{numero}", "2");
		
		new JobsNowRequestInterface(url, this.client)._delete(pathVariables);
		
		verify(this.client, times(1))._doDelete(urlEsperada);
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveExecutarOPatch() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro";
		Map<String, String> pathVariables = new HashMap<>();
		Map<String, String> corpoRequisicao = new HashMap<>();
		
		pathVariables.put("{id}", "1");
		corpoRequisicao.put("teste", "1");
		corpoRequisicao.put("palavra", "teste");
		
		new JobsNowRequestInterface(url, this.client)._patch(pathVariables, corpoRequisicao);
		
		verify(this.client, times(1))._doPatch(urlEsperada, corpoRequisicao);
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveRetornarIdGeradoAoExecutarPost() {
		String url = "localhost:8080/jobsnow/teste/{id}/outro";
		String urlEsperada = "localhost:8080/jobsnow/teste/1/outro";
		Map<String, String> pathVariables = new HashMap<>();
		Map<String, String> corpoRequisicao = new HashMap<>();
		
		pathVariables.put("{id}", "1");
		corpoRequisicao.put("teste", "1");
		corpoRequisicao.put("palavra", "teste");

		String conteudo = "7";
		when(this.client._doPost(urlEsperada, corpoRequisicao))
			.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
		
		Long id = new JobsNowRequestInterface(url, this.client)._post(pathVariables, corpoRequisicao);
		
		assertEquals(7, id.intValue());
		verify(this.client, times(1))._doPost(urlEsperada, corpoRequisicao);
		verifyNoMoreInteractions(this.client);
	}
}