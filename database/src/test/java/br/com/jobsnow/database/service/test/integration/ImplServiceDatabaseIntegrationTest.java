package br.com.jobsnow.database.service.test.integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.jobsnow.database.DatabaseApplication;
import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.params.EspecificacaoCampo;
import br.com.jobsnow.database.params.FuncaoAgregacao;
import br.com.jobsnow.database.params.Join;
import br.com.jobsnow.database.service.ServiceDatabase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ImplServiceDatabaseIntegrationTest {

	@Autowired
	private ServiceDatabase srvDatabase;
	
	@Test
	public void _deveSelecionarVariosRegistros() throws Exception {
		String[] camposParaOrdenacao = null;
		String[] camposSelect = {"id", "status", "remetente", "email"};
		String tabela = "entrevista";
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();

		joins.add(new Join("entrevista", "id_candidato", "usuario", "id_usuario"));
		restricoes.add(new EspecificacaoCampo("id", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,joins,restricoes,camposParaOrdenacao);
		
		List<Map<String, String>> resultado = this.srvDatabase._selecioneVariosRegistros(params);
		
		List<Map<String, String>> retornoEsperado = new ArrayList<>();
		Map<String, String> ret = new HashMap<>();
		ret.put("id", "2");
		ret.put("status", "15");
		ret.put("remetente", "17");
		ret.put("email", "teste@teste.com");
		retornoEsperado.add(ret);
		
		assertThat(retornoEsperado, is(resultado));
	}
	
	@Test
	public void _deveSelecionarUmRegistro() throws Exception {
		String[] camposSelect = {"id", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		Long idRegistro = 1L;
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);
		Map<String,String> retorno = this.srvDatabase._selecioneUmUnicoRegistro(params);
		
		Map<String, String> esperado = new HashMap<>();
		esperado.put("id", "1");
		esperado.put("status", "15");
		esperado.put("remetente", "17");
		esperado.put("id_candidato", "1");
		
		assertEquals(esperado, retorno);
	}
	
	@Test
	public void _deveAtualizarUmUnicoRegistro() throws Exception {
		String camposSelect[] = {"status", "remetente"};
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		Long idRegistro = 4L;
		
		novosValores.add(new EspecificacaoCampo("status", "15", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "17", "Integer"));

		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores, null, idRegistro);
		this.srvDatabase._atualizarRegistros(params);

		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);
		
		Map<String, String> registroAlterado = this.srvDatabase._selecioneUmUnicoRegistro(params);
		
		Map<String, String> valores = new HashMap<>();
		
		valores.put("status", "15");
		valores.put("remetente", "17");
		
		assertEquals(valores, registroAlterado);
	}
	
	@Test
	public void _deveAlterarStatusDeUmUnicoRegistro() throws Exception {
		Long idRegistro = 5L;
		String[] camposSelect = {"status"};
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		novosValores.add(new EspecificacaoCampo("status", "45", "Integer"));
		
		DatabaseParamsDTO params =  new DatabaseParamsDTO(tabela, novosValores,null,idRegistro);
		
		this.srvDatabase._atualizarRegistros(params);
		
		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);
		Map<String, String> registroAlterado = this.srvDatabase._selecioneUmUnicoRegistro(params);
		Map<String, String> esperado = new HashMap<>();
		esperado.put("status", "45");
		
		assertEquals(esperado, registroAlterado);
	}
	
	@Ignore
	@Test
	public void _deveInserirUmUnicoRegistro() throws Exception {
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		
		novosValores.add(new EspecificacaoCampo("id", "9", "Integer"));
		novosValores.add(new EspecificacaoCampo("status", "15", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "22", "Integer"));
		novosValores.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores);
		
		Long idGerado = this.srvDatabase._inserirUmUnicoRegistro(params);
		
		assertEquals(idGerado.intValue(), 9);
	}

	@Test
	public void _deveObterTotal() throws Exception {
		String tabela = "entrevista";
		String[] camposParaSelecionar = null; 
		String[] camposAgrupamento = null;
		LinkedHashSet<FuncaoAgregacao> funcoesAgregacao = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		LinkedHashSet<Join> joins = null;
		
		funcoesAgregacao.add(new FuncaoAgregacao("count", "*"));
		restricoes.add(new EspecificacaoCampo("status", "15", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposParaSelecionar,funcoesAgregacao,tabela,joins,restricoes,camposAgrupamento);

		Map<String, String> total = this.srvDatabase._obterTotais(params);
		
		Map<String, String> totalEsperado = new HashMap<>();
		totalEsperado.put("count", "2");
		
		assertEquals(total, totalEsperado);
	}
	
	@Test
	public void _deveVerificarExistenciaDeRegistrosDadasEstasRestricoes() throws Exception {
		String[] camposParaOrdenacao = null;
		String[] camposSelect = null;
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("status", "15", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
		
		boolean resultado = this.srvDatabase._verificarExistenciaDeRegistrosDadasEstasRestricoes(params);
		
		assertTrue(resultado);
	}
	
	@Test
	public void _deveTrazerTodosOsCamposDaTabela() {
		String tabela = "entrevista";
		Set<String> camposEsperados = new HashSet<>();
		camposEsperados.add("id");
		camposEsperados.add("status");
		camposEsperados.add("remetente");
		camposEsperados.add("id_candidato");
		camposEsperados.add("local");
		camposEsperados.add("data");
		
		Set<String> camposTabela = this.srvDatabase._getCamposTabela(tabela, "information_schema.columns");
		
		assertEquals(camposEsperados, camposTabela);
	}
	
	@Test
	public void _deveAtualizarRegistrosEmLote() {
		String camposSelect[] = {"id", "status", "remetente"};
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		Long idRegistro = null;
		
		novosValores.add(new EspecificacaoCampo("status", "25", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "27", "Integer"));

		restricoes.add(new EspecificacaoCampo("id_candidato", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores, restricoes, idRegistro);
		this.srvDatabase._atualizarRegistros(params);

		String[] camposParaOrdenacao = {"id"};
		params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
		
		List<Map<String,String>> registroAlterado = this.srvDatabase._selecioneVariosRegistros(params);
		List<Map<String, String>> valores = new ArrayList<>();
		Map<String, String> retorno = new HashMap<>();
		
		retorno.put("id", "3");
		retorno.put("status", "25");
		retorno.put("remetente", "27");
		valores.add(retorno);
		
		retorno = new HashMap<>();
		retorno.put("id", "4");
		retorno.put("status", "25");
		retorno.put("remetente", "27");
		
		valores.add(retorno);

		retorno = new HashMap<>();
		retorno.put("id", "5");
		retorno.put("status", "25");
		retorno.put("remetente", "27");
		
		valores.add(retorno);
		
		assertEquals(valores, registroAlterado);
	}
	
	@Ignore
	@Test
	public void _deveInserirRegistrosEmLote() {
		String tabela = "entrevista";
		List<LinkedHashSet<EspecificacaoCampo>> novosValores = new LinkedList<>();
		LinkedHashSet<EspecificacaoCampo> insert1 = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> insert2 = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> insert3 = new LinkedHashSet<>();
		
		insert1.add(new EspecificacaoCampo("id", "6", "Integer"));
		insert1.add(new EspecificacaoCampo("status", "11", "Integer"));
		insert1.add(new EspecificacaoCampo("remetente", "10", "Integer"));
		insert1.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		novosValores.add(insert1);
		
		insert2.add(new EspecificacaoCampo("id", "7", "Integer"));
		insert2.add(new EspecificacaoCampo("status", "12", "Integer"));
		insert2.add(new EspecificacaoCampo("remetente", "10", "Integer"));
		insert2.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		novosValores.add(insert2);
		
		insert3.add(new EspecificacaoCampo("id", "8", "Integer"));
		insert3.add(new EspecificacaoCampo("status", "13", "Integer"));
		insert3.add(new EspecificacaoCampo("remetente", "10", "Integer"));
		insert3.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		novosValores.add(insert3);
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores);
		
		this.srvDatabase._inserirRegistrosEmBatch(params);

		String camposSelect[] = {"id", "status", "remetente"};
		String[] camposParaOrdenacao = {"id"};
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("remetente", "10", "Integer"));
		
		params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
		List<Map<String,String>> registroAlterado = this.srvDatabase._selecioneVariosRegistros(params);
		List<Map<String, String>> valores = new ArrayList<>();
		Map<String, String> retorno = new HashMap<>();
		
		retorno.put("id", "6");
		retorno.put("status", "11");
		retorno.put("remetente", "10");
		valores.add(retorno);
		
		retorno = new HashMap<>();
		retorno.put("id", "7");
		retorno.put("status", "12");
		retorno.put("remetente", "10");
		
		valores.add(retorno);

		retorno = new HashMap<>();
		retorno.put("id", "8");
		retorno.put("status", "13");
		retorno.put("remetente", "10");
		
		valores.add(retorno);
		
		assertEquals(valores, registroAlterado);
	}
}