package br.com.jobsnow.database_client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database_client.DatabaseInterface;
import br.com.jobsnow.database_client.client.Client;
import br.com.jobsnow.database_client.params.EspecificacaoCampo;
import br.com.jobsnow.database_client.params.FuncaoAgregacao;
import br.com.jobsnow.database_client.params.RequestParamsDTO;

public class DatabaseInterfaceTest {
	private Client client;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setup() {
		this.client = mock(Client.class);
	}
	
	@Test
	public void naoDeveSelecionarVariosRegistrosSemTabelaPreenchida() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para executar esta tarefa é necessário preencher a tabela.");
		String[] ordenacao = null;
		new DatabaseInterface(null, this.client, new HashSet<>())._selecioneVariosRegistros(new String[] {}, null, null,ordenacao);
	}
	
	@Test
	public void naoDeveSelecionarVariosRegistrosSemCamposSelect() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para executar esta tarefa é necessário preencher o camposSelect.");
		String[] ordenacao = null;
		new DatabaseInterface("teste",this.client, new HashSet<>())._selecioneVariosRegistros(new String[] {}, null, null,ordenacao);
	}

	@Test
	public void deveMostrarMensagemDeFalhaQuandoStatusNaoFor200() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("");
	
		doThrow(new RuntimeException(""))
			.when(this.client)._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		
		String[] campos = {"teste"};
		Set<String> camposTabela = new HashSet<>();
		camposTabela.add("teste");
		
		new DatabaseInterface("teste",this.client, camposTabela)._selecioneVariosRegistros(campos, null, null,null,null);
	}
	
	@Test
	public void naoDeveSelecionarUmUnicoRegistroSemIdRegistroPreenchido() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para executar esta tarefa é necessário preencher o idRegistro.");
		String[] campos = {"teste"};
		new DatabaseInterface("teste",this.client, new HashSet<>())._selecioneUmUnicoRegistro(campos, null, null);
	}
	
	@Test
	public void naoDeveAtualizarUmUnicoRegistroSemCamposMaisSeusNovosValoresPreenchidos() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para executar esta tarefa é necessário preencher os camposMaisSeusNovosValores.");
		new DatabaseInterface("teste",1L, this.client, new HashSet<>())._atualizarUmUnicoRegistro(new LinkedHashSet<>(), null);
	}
	
	@Test
	public void naoDeveAtualizarUmUnicoRegistroComCamposErrados() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("");
		
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		novosValores.add(new EspecificacaoCampo("teste", "15", "Integer"));

	    Set<String> teste = new HashSet<>();
	    String conteudoLista = new ObjectMapper().writeValueAsString(teste);
	    when(this.client._doGet(Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudoLista).array()));
		
		doThrow(new RuntimeException(""))
			.when(this.client)._doPatch(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		
		new DatabaseInterface("entrevista", 5L,this.client, new HashSet<>())._atualizarUmUnicoRegistro(novosValores, null);
	}
	
	@Test
	public void naoDeveAlterarStatusUmUnicoRegistroComCamposErrados() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("");
		
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		novosValores.add(new EspecificacaoCampo("teste", "15", "Integer"));
		
	    Set<String> teste = new HashSet<>();
	    String conteudoLista = new ObjectMapper().writeValueAsString(teste);
	    when(this.client._doGet(Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudoLista).array()));
		
		doThrow(new RuntimeException(""))
			.when(this.client)._doDelete(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		
		new DatabaseInterface("entrevista", 5L,this.client, new HashSet<>())._alterarStatusDeUmUnicoRegistro(novosValores, null);
	}
	
	@Test
	public void deveSelecionarVariosRegistros() throws Exception {
        String[] camposSelect = {"id_teste", "teste"};
        HashSet<String> camposTabela = new HashSet<>();
        LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<EspecificacaoCampo>();
        EspecificacaoCampo esp = new EspecificacaoCampo("id_teste", "2", "Integer");
        
        restricoes.add(esp);
        
        esp = new EspecificacaoCampo("id_teste", "3", "Integer");
        
        restricoes.add(esp);
        camposTabela.add("id_teste");
        camposTabela.add("teste");
        
		List<Map<String, String>> registrosEsperados = new ArrayList<Map<String, String>>();
		Map<String, String> ret = new HashMap<String, String>();
		ret.put("id_teste", "2");
		ret.put("teste", "2");
		registrosEsperados.add(ret);
		
		ret = new HashMap<String, String>();
		ret.put("id_teste", "3");
		ret.put("teste", "10");
		registrosEsperados.add(ret);

		String conteudo = new ObjectMapper().writeValueAsString(registrosEsperados);
	    when(this.client._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString()))
	    	.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
		
	    String[] ordenacao = {};
		List<Map<String, String>> registros = new DatabaseInterface("entrevista",this.client, camposTabela)
				._selecioneVariosRegistros(camposSelect, null, restricoes,ordenacao);
        
		assertEquals(registrosEsperados, registros);
		verify(this.client, times(1))._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}

	@Test
	public void deveSelecionarUmUnicoRegistro() throws Exception {
        String[] camposSelect = {"id_teste", "teste"};
        HashSet<String> camposTabela = new HashSet<>();
        camposTabela.add("id_teste");
        camposTabela.add("teste");
        
		Map<String, String> registrosEsperados = new HashMap<String, String>();
		registrosEsperados.put("id_teste", "2");
		registrosEsperados.put("teste", "0");

		String conteudo = new ObjectMapper().writeValueAsString(registrosEsperados);
	    when(this.client._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));

	    Set<String> teste = new HashSet<>();
	    String conteudoLista = new ObjectMapper().writeValueAsString(teste);
	    when(this.client._doGet(Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudoLista).array()));
	    
		Map<String, String> registros = new DatabaseInterface("entrevista",2L,this.client, camposTabela)._selecioneUmUnicoRegistro(camposSelect, null, null);
        
		assertEquals(registrosEsperados, registros);
		verify(this.client)._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}

	@Test
	public void deveObterTotal() throws Exception {
		LinkedHashSet<FuncaoAgregacao> funcao = new LinkedHashSet<>();
		
		funcao.add(new FuncaoAgregacao("count", "*"));
		
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<EspecificacaoCampo>();
        EspecificacaoCampo esp = new EspecificacaoCampo("id_teste", "2", "Integer");
        
        restricoes.add(esp);
        
        esp = new EspecificacaoCampo("id_teste", "3", "Integer");
        
        restricoes.add(esp);
		
		Map<String, Integer> registrosEsperados = new HashMap<String, Integer>();
		registrosEsperados.put("total", 2);
		
		String conteudo = new ObjectMapper().writeValueAsString(registrosEsperados);
	    when(this.client._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudo).array()));
		
		Map<String, Integer> registros = new DatabaseInterface("entrevista",this.client, new HashSet<>())._obterTotais(funcao, null, restricoes);
        
		assertEquals(registrosEsperados, registros);
		verify(this.client, times(1))._doGet(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveRetornarTrueVerificarExistenciaDadasEstasRestricoes() throws Exception {
	    when(this.client._doHead(Mockito.any(RequestParamsDTO.class), Mockito.anyString())).thenReturn(true);
	    
		boolean existeRegistros = new DatabaseInterface("entrevista",this.client,null)
	    		._verificarExistenciaDeRegistrosDadasEstasRestricoes(null, null);
        
        assertTrue(existeRegistros);
        verify(this.client, times(1))._doHead(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}

	@Test
	public void deveRetornarFalseVerificarExistenciaDadasEstasRestricoes() throws Exception {
		when(this.client._doHead(Mockito.any(RequestParamsDTO.class), Mockito.anyString())).thenReturn(false);
		
        boolean existeRegistros = new DatabaseInterface("entrevista",this.client,null)
        		._verificarExistenciaDeRegistrosDadasEstasRestricoes(null, null);
        
        assertFalse(existeRegistros);
        verify(this.client, times(1))._doHead(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveAtualizarUmUnicoRegistro() throws Exception {
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		HashSet<String> camposTabela = new HashSet<>();

		novosValores.add(new EspecificacaoCampo("teste", "15", "Integer"));
		camposTabela.add("teste");
		
		Set<String> teste = camposTabela;
	    String conteudoLista = new ObjectMapper().writeValueAsString(teste);
	    when(this.client._doGet(Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudoLista).array()));
		
		new DatabaseInterface("entrevista", 5L,this.client, camposTabela)._atualizarUmUnicoRegistro(novosValores, null);

		verify(this.client)._doPatch(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveAlterarStatusUmUnicoRegistro() throws Exception {
		HashSet<String> camposTabela = new HashSet<>();
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		
		novosValores.add(new EspecificacaoCampo("teste", "7", "Integer"));
		camposTabela.add("teste");
		
	    Set<String> teste = new HashSet<>();
	    String conteudoLista = new ObjectMapper().writeValueAsString(teste);
	    when(this.client._doGet(Mockito.anyString())).thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode(conteudoLista).array()));
		
		new DatabaseInterface("entrevista", 5L,this.client, camposTabela)._alterarStatusDeUmUnicoRegistro(novosValores, null);

		verify(this.client)._doDelete(Mockito.any(RequestParamsDTO.class), Mockito.anyString());
		verifyNoMoreInteractions(this.client);
	}
	
	@Test
	public void deveInserirUmUnicoRegistro() throws Exception {
		HashSet<String> camposTabela = new HashSet<>();
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
        
		novosValores.add(new EspecificacaoCampo("id_teste", "6", "Integer"));
        camposTabela.add("id_teste");
        
        when(this.client._doPost(Mockito.any(RequestParamsDTO.class), Mockito.anyString())).thenReturn(6L);
        
        Long idRegistro = new DatabaseInterface("entrevista",this.client, camposTabela)._inserirUmUnicoRegistro(novosValores);
        
        assertEquals(6, idRegistro.intValue());
	}
}