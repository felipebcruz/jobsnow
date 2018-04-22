package br.com.jobsnow.database.service.test.integration;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
		String[] camposSelect = {"id_entrevista", "status", "remetente", "email"};
		String tabela = "entrevista";
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();

		joins.add(new Join("entrevista", "id_candidato", "usuario", "id_usuario"));
		restricoes.add(new EspecificacaoCampo("id_entrevista", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,joins,restricoes,null);
		
		List<Map<String, String>> resultado = this.srvDatabase._selecioneVariosRegistros(params);
		
		List<Map<String, String>> retornoEsperado = new ArrayList<>();
		Map<String, String> ret = new HashMap<>();
		ret.put("id_entrevista", "2");
		ret.put("status", "2");
		ret.put("remetente", "0");
		ret.put("email", "teste@teste.com");
		retornoEsperado.add(ret);
		
		assertThat(retornoEsperado, is(resultado));
	}
	
	@Test
	public void _deveSelecionarUmRegistro() throws Exception {
		String[] camposSelect = {"id_entrevista", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		Long idRegistro = 1L;
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro,null);
		Map<String,String> retorno = this.srvDatabase._selecioneUmUnicoRegistro(params);
		
		Map<String, String> esperado = new HashMap<>();
		esperado.put("id_entrevista", "1");
		esperado.put("status", "2");
		esperado.put("remetente", "0");
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
		this.srvDatabase._atualizarUmUnicoRegistro(params);

		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro,null);
		
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
		
		this.srvDatabase._atualizarUmUnicoRegistro(params);
		
		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro,null);
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
		
		novosValores.add(new EspecificacaoCampo("id_entrevista", "6", "Integer"));
		novosValores.add(new EspecificacaoCampo("status", "11", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "3", "Integer"));
		novosValores.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores);
		
		Long idGerado = this.srvDatabase._inserirUmUnicoRegistro(params);
		
		assertEquals(idGerado.intValue(), 6);
	}

	@Test
	public void _deveObterTotal() throws Exception {
		String tabela = "entrevista";
		String[] camposAgrupamento = null;
		LinkedHashSet<FuncaoAgregacao> funcoesAgregacao = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		LinkedHashSet<Join> joins = null;
		
		funcoesAgregacao.add(new FuncaoAgregacao("count", "*"));
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(funcoesAgregacao,tabela,joins,restricoes,camposAgrupamento);

		Map<String, String> total = this.srvDatabase._obterTotais(params);
		
		Map<String, String> totalEsperado = new HashMap<>();
		totalEsperado.put("count", "2");
		
		assertEquals(total, totalEsperado);
	}
	
	@Test
	public void _deveVerificarExistenciaDeRegistrosDadasEstasRestricoes() throws Exception {
		String[] camposSelect = {"id_entrevista", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,null);
		
		boolean resultado = this.srvDatabase._verificarExistenciaDeRegistrosDadasEstasRestricoes(params);
		
		assertTrue(resultado);
	}
	
	@Test
	public void _deveTrazerTodosOsCamposDaTabela() {
		String tabela = "entrevista";
		Set<String> camposEsperados = new HashSet<>();
		camposEsperados.add("id_entrevista");
		camposEsperados.add("status");
		camposEsperados.add("remetente");
		camposEsperados.add("id_candidato");
		camposEsperados.add("local");
		camposEsperados.add("data");
		
		Set<String> camposTabela = this.srvDatabase._getCamposTabela(tabela, "information_schema.columns");
		
		assertEquals(camposEsperados, camposTabela);
	}
}