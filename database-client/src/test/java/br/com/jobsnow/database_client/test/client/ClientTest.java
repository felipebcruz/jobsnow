package br.com.jobsnow.database_client.test.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database_client.client.Client;
import br.com.jobsnow.database_client.client.HttpDeleteWithBody;

public class ClientTest {
	private HttpClient client;
	private HttpResponse response;
	private StatusLine statusLine;
	private HttpGet get;
	private HttpPost post;
	private HttpPatch patch;
	private HttpDeleteWithBody delete;
	private HttpHead head;
	private HttpEntity entity;
	private Header header;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void _setup() {
		this.client = mock(HttpClient.class);
		this.response = mock(HttpResponse.class);
		this.statusLine = mock(StatusLine.class);
		this.get = mock(HttpGet.class);
		this.post = mock(HttpPost.class);
		this.patch = mock(HttpPatch.class);
		this.head = mock(HttpHead.class);
		this.entity = mock(StringEntity.class);
		this.delete = mock(HttpDeleteWithBody.class);
		this.header = mock(Header.class);
	}
	
	@Test
	public void _deveExecutarDoGetComSucesso() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(200);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.get)).thenReturn(this.response);
        when(this.entity.getContent()).thenReturn(null);
        when(this.response.getEntity()).thenReturn(this.entity);
        
        new Client(this.client, this.response, this.get)._doGet(null, "");

        assertEquals(this.statusLine.getStatusCode(), 200);
        assertEquals(this.entity.getContent(), null);
        verify(this.client, times(1)).execute(this.get);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveLancarNullPointerExceptionComUrlNull() throws Exception {
		this.expectedEx.expect(NullPointerException.class);
		new Client()._doGet(null, null);
	}
	
	@Test
	public void _deveLancarClientProtocolExceptionComUrlVazia() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		new Client()._doGet(null, "");
	}
	
	@Test
	public void _deveMostrarMensagemDeErroQuandoStatusDoGetNaoFor200() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Falha ao fazer a requisição, HTTP error code: " + 400 + ", motivo do erro: ");
		when(this.statusLine.getStatusCode()).thenReturn(400);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.get)).thenReturn(this.response);
        
        Map<String, String> teste = new HashMap<String, String>();
        teste.put("message", "");
        
        String conteudo = new ObjectMapper().writeValueAsString(teste);
        
        when(this.entity.getContent()).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
        when(this.response.getEntity()).thenReturn(this.entity);
        
        new Client(this.client, this.response, this.get)._doGet(null, "");
	}
	
	@Test
	public void _deveExecutarDoHeadERetornarTrue() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(200);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.head)).thenReturn(this.response);
        
        boolean resultado = new Client(this.client, this.response, this.head)._doHead(null, "");

        assertTrue(resultado);
        assertEquals(this.statusLine.getStatusCode(), 200);
        verify(this.client, times(1)).execute(this.head);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveExecutarDoHeadERetornarFalse() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(404);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.head)).thenReturn(this.response);
        
        
        boolean resultado = new Client(this.client, this.response, this.head)._doHead(null, "");

        assertFalse(resultado);
        assertEquals(this.statusLine.getStatusCode(), 404);
        verify(this.client, times(1)).execute(this.head);
        verifyNoMoreInteractions(this.client);
	}

	@Test
	public void _deveExecutarDoHeadERetornarMensagemDeHttpErrorCode() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(400);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.head)).thenReturn(this.response);
        this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("");

		Map<String, String> teste = new HashMap<String, String>();
        teste.put("message", "");
        
        String conteudo = new ObjectMapper().writeValueAsString(teste);
        
        when(this.entity.getContent()).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
        when(this.response.getEntity()).thenReturn(this.entity);
		
        new Client(this.client, this.response, this.head)._doHead(null, "");

        assertEquals(this.statusLine.getStatusCode(), 404);
        verify(this.client, times(1)).execute(this.head);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveExecutarDoPatchERetornarStatusNoContent() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(204);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.patch)).thenReturn(this.response);
        
        new Client(this.client, this.response, this.patch)._doPatch(null, "");

        assertEquals(this.statusLine.getStatusCode(), 204);
        verify(this.client, times(1)).execute(this.patch);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveExecutarDoPatchERetornarMensagemDeHttpErrorCode() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(400);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.patch)).thenReturn(this.response);
        this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Falha ao fazer a requisição, HTTP error code: "+400+ ", motivo do erro: ");
        
		Map<String, String> teste = new HashMap<String, String>();
        teste.put("message", "");
        
        String conteudo = new ObjectMapper().writeValueAsString(teste);
        
        when(this.entity.getContent()).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
        when(this.response.getEntity()).thenReturn(this.entity);

		new Client(this.client, this.response, this.patch)._doPatch(null, "");

        assertEquals(this.statusLine.getStatusCode(), 400);
        verify(this.client, times(1)).execute(this.patch);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveExecutarDoDeleteERetornarStatusNoContent() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(204);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.delete)).thenReturn(this.response);
        
        
        new Client(this.client, this.response, this.delete)._doDelete(null, "");

        assertEquals(this.statusLine.getStatusCode(), 204);
        verify(this.client, times(1)).execute(this.delete);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveExecutarDoDeleteERetornarMensagemDeHttpErrorCode() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(400);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.delete)).thenReturn(this.response);
        this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Falha ao fazer a requisição, HTTP error code: "+400+ ", motivo do erro: ");
		
		Map<String, String> teste = new HashMap<String, String>();
        teste.put("message", "");
        
        String conteudo = new ObjectMapper().writeValueAsString(teste);
        
        when(this.entity.getContent()).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
        when(this.response.getEntity()).thenReturn(this.entity);
        
        new Client(this.client, this.response, this.delete)._doDelete(null, "");

        assertEquals(this.statusLine.getStatusCode(), 400);
        verify(this.client, times(1)).execute(this.delete);
        verifyNoMoreInteractions(this.client);
	}

	@Test
	public void _deveExecutarDoPostERetornarIdGerado() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(201);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.post)).thenReturn(this.response);
        when(this.header.getValue()).thenReturn("http://localhost:8080/api/v1/database/registros/6");
        when(this.response.getFirstHeader("location")).thenReturn(this.header);
        
        Long idGerado = new Client(this.client, this.response, this.post)._doPost(null, "");

        assertEquals(idGerado.intValue(), 6);
        assertEquals(this.statusLine.getStatusCode(), 201);
        verify(this.client, times(1)).execute(this.post);
        verifyNoMoreInteractions(this.client);
	}

	@Test
	public void _deveExecutarDoPostERetornarMensagemDeHttpErrorCode() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(400);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.post)).thenReturn(this.response);
        this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Falha ao fazer a requisição, HTTP error code: "+400+ ", motivo do erro: ");
		Map<String, String> teste = new HashMap<String, String>();
        teste.put("message", "");
        
        String conteudo = new ObjectMapper().writeValueAsString(teste);
        
        when(this.entity.getContent()).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
        when(this.response.getEntity()).thenReturn(this.entity);
		
        new Client(this.client, this.response, this.post)._doPost(null, "");

        assertEquals(this.statusLine.getStatusCode(), 400);
        verify(this.client, times(1)).execute(this.post);
        verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void _deveExecutarDoGetApenasUrl() throws Exception {
		when(this.statusLine.getStatusCode()).thenReturn(200);
		when(this.response.getStatusLine()).thenReturn(this.statusLine);
        when(this.client.execute(this.get)).thenReturn(this.response);
        when(this.entity.getContent()).thenReturn(null);
        when(this.response.getEntity()).thenReturn(this.entity);

        new Client(this.client, this.response, this.get)._doGet("");
        
        assertEquals(this.statusLine.getStatusCode(), 200);
        assertEquals(this.entity.getContent(), null);
        verify(this.client, times(1)).execute(this.get);
        verifyNoMoreInteractions(this.client);
	}
}