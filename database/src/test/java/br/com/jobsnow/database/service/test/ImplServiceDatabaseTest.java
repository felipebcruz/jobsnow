package br.com.jobsnow.database.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.jobsnow.database.DatabaseApplication;
import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.params.DatabaseParamsDTO.AusenciaDeCamposMaisValoresException;
import br.com.jobsnow.database.params.DatabaseParamsDTO.EntidadeSemTabelaException;
import br.com.jobsnow.database.params.DatabaseParamsDTO.IDDestaEntidadeDeveriaEstarPresenteException;
import br.com.jobsnow.database.params.EspecificacaoCampo;
import br.com.jobsnow.database.params.FuncaoAgregacao;
import br.com.jobsnow.database.params.Join;
import br.com.jobsnow.database.service.ServiceDatabase;
import br.com.jobsnow.database.service.impl.ImplServiceDatabase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ImplServiceDatabaseTest {

	private ServiceDatabase srvDatabase;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Before
	public void _setUp() {
		this.srvDatabase = new ImplServiceDatabase(this.jdbcTemplate, this.namedParameterJdbcTemplate);
	}

	@Test
	public void _quandoParamsEstiverNullEmMontarOSQL() throws Exception {
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("Os parametros do banco de dados nao foram informados");
		this.srvDatabase._selecioneVariosRegistros(null);
	}

	@Test
	public void _quandoTabelaEstiverNullEmMontarSQL() throws Exception {
		DatabaseParamsDTO params = new DatabaseParamsDTO();
		
		this.expectedEx.expect(EntidadeSemTabelaException.class);
		this.srvDatabase._selecioneVariosRegistros(params);
	}

	@Test
	public void _quandoIdDesteRegistroEstiverNullSelecioneUmUnicoRegistro() throws Exception {
		DatabaseParamsDTO params = new DatabaseParamsDTO("teste", new LinkedHashSet<>());
		
		this.expectedEx.expect(IDDestaEntidadeDeveriaEstarPresenteException.class);
		this.srvDatabase._selecioneUmUnicoRegistro(params);
	}
	
	@Test
	public void _quandoOsCamposDoUpdateEstiveremNull() throws Exception {
		DatabaseParamsDTO params = new DatabaseParamsDTO("teste",new LinkedHashSet<>());
		
		this.expectedEx.expect(AusenciaDeCamposMaisValoresException.class);
		this.srvDatabase._atualizarRegistros(params);
	}
	
	@Test
	public void _quandoIdRegistroEstiverNullNoUpdate() throws Exception {
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = new LinkedHashSet<>();
		camposMaisSeusNovosValores.add(new EspecificacaoCampo());
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela,camposMaisSeusNovosValores,null,null);
		
		this.expectedEx.expect(IDDestaEntidadeDeveriaEstarPresenteException.class);
		this.srvDatabase._atualizarRegistros(params);
	}
	
	@Test
	public void _quandoTabelaEstiverNullEmAtualizarUmRegistro() throws Exception {
		DatabaseParamsDTO params = new DatabaseParamsDTO(null,null,null,null);
		
		this.expectedEx.expect(EntidadeSemTabelaException.class);
		this.srvDatabase._atualizarRegistros(params);
	}
	
	@Test
	public void _quandoCamposDoInsertEstiveremNull() throws Exception {
		DatabaseParamsDTO params = new DatabaseParamsDTO("teste", new LinkedHashSet<>());

		this.expectedEx.expect(AusenciaDeCamposMaisValoresException.class);
		this.srvDatabase._inserirUmUnicoRegistro(params);
	}
	
	@Test
	public void _quandoParamsEstiverNullEmInserirUmUnicoRegistro() throws Exception {
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("Os parametros do banco de dados nao foram informados");
		this.srvDatabase._inserirUmUnicoRegistro(null);
	}

	@Test
	public void _quandoTabelaEstiverNullEmInserirUmUnicoRegistro() throws Exception {
		DatabaseParamsDTO params = new DatabaseParamsDTO(null, new LinkedHashSet<>());
		
		this.expectedEx.expect(EntidadeSemTabelaException.class);
		this.srvDatabase._inserirUmUnicoRegistro(params);
	}
	
	@Test
	public void _quandoParamsEstiverNullEmAtualizarUmUnicoRegistro() throws Exception {
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("Os parametros do banco de dados nao foram informados");
		this.srvDatabase._atualizarRegistros(null);
	}
	
	@Test
	public void _quandoTabelaEstiverNullEmGetCamposTabela() {
		this.expectedEx.expect(EntidadeSemTabelaException.class);
		this.srvDatabase._getCamposTabela(null, "information_schema.columns");
	}
	
	@Test
	public void _deveSelecionarVariosRegistros() throws Exception {
		String[] camposSelect = {"id", "status", "remetente", "email"};
		String[] camposParaOrdenacao = null;
		String tabela = "entrevista";
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();

		joins.add(new Join("entrevista", "id_candidato", "usuario", "id_usuario"));
		restricoes.add(new EspecificacaoCampo("id", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,joins,restricoes,camposParaOrdenacao);

		List<Map<String, Object>> retornoEsperado = new ArrayList<>();
		Map<String, Object> ret = new HashMap<>();
		ret.put("id", "2");
		ret.put("status", "2");
		ret.put("remetente", "0");
		ret.put("id_candidato", "1");
		retornoEsperado.add(ret);

		when(this.jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(retornoEsperado);

		List<Map<String, String>> resultado = this.srvDatabase._selecioneVariosRegistros(params);

		assertEquals(retornoEsperado, resultado);
		verify(this.jdbcTemplate, times(1)).queryForList(Mockito.anyString());
		verifyNoMoreInteractions(this.jdbcTemplate);
	}
	
	@Test
	public void _deveSelecionarUmRegistro() throws Exception {
		String tabela = "entrevista";
		String[] camposSelect = {"id", "status", "remetente", "id_candidato"};
		Long idRegistro = 1L;
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);

		Map<String, Object> esperado = new HashMap<>();
		esperado.put("id", "1");
		esperado.put("status", "2");
		esperado.put("remetente", "0");
		esperado.put("id_candidato", "1");

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(esperado);
		
		Map<String, String> retorno = this.srvDatabase._selecioneUmUnicoRegistro(params);

		assertEquals(esperado, retorno);
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verifyNoMoreInteractions(this.jdbcTemplate);
	}

	@Test
	public void _deveAtualizarUmUnicoRegistro() throws Exception {
		String camposSelect[] = {"status", "remetente"};
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		Long idRegistro = 4L;
		Map<String, Object> valores = new HashMap<>();

		valores.put("status", "9");
		valores.put("remetente", "7");
		
		novosValores.add(new EspecificacaoCampo("status", "15", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "17", "Integer"));

		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores, null, idRegistro);

		this.srvDatabase._atualizarRegistros(params);

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(valores);
		
		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);

		Map<String, String> registroAlterado = this.srvDatabase._selecioneUmUnicoRegistro(params);

		assertEquals(valores, registroAlterado);
		verify(this.namedParameterJdbcTemplate, times(1)).update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class));
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verifyNoMoreInteractions(this.namedParameterJdbcTemplate);
		verifyNoMoreInteractions(this.jdbcTemplate);
	}

	@Test
	public void _deveAlterarStatusDeUmUnicoRegistro() throws Exception {
		Long idRegistro = 5L;
		String[] camposSelect = {"status"};
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		novosValores.add(new EspecificacaoCampo("status", "45", "Integer"));
		
		DatabaseParamsDTO params =  new DatabaseParamsDTO(tabela, novosValores,null,idRegistro);
		
		Map<String, Object> esperado = new HashMap<>();
		esperado.put("status", "45");

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(esperado);

		this.srvDatabase._atualizarRegistros(params);

		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);
		Map<String, String> registroAlterado = this.srvDatabase._selecioneUmUnicoRegistro(params);

		assertEquals(esperado, registroAlterado);
		verify(this.namedParameterJdbcTemplate, times(1)).update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class));
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verifyNoMoreInteractions(this.jdbcTemplate);
		verifyNoMoreInteractions(this.namedParameterJdbcTemplate);
	}

	@Ignore
	@Test
	public void _deveInserirUmUnicoRegistro() throws Exception {
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		
		novosValores.add(new EspecificacaoCampo("id", "6", "Integer"));
		novosValores.add(new EspecificacaoCampo("status", "11", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "3", "Integer"));
		novosValores.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		KeyHolder holder = Mockito.mock(GeneratedKeyHolder.class);
		when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class),Mockito.any(GeneratedKeyHolder.class), Mockito.any(String[].class))).thenReturn(5);
		when(holder.getKey().longValue()).thenReturn(5L);

		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores);

		Long idGerado = this.srvDatabase._inserirUmUnicoRegistro(params);

		assertEquals(idGerado.intValue(), 6);
		verify(this.jdbcTemplate, times(1)).update(Mockito.anyString(),Mockito.any(MapSqlParameterSource.class), Mockito.any(GeneratedKeyHolder.class), Mockito.any());
		verifyNoMoreInteractions(this.jdbcTemplate);
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
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposParaSelecionar,funcoesAgregacao,tabela,joins,restricoes,camposAgrupamento);
		
		Map<String, Object> totalEsperado = new HashMap<>();
		totalEsperado.put("count", "2");

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(totalEsperado);

		Map<String, String> total = this.srvDatabase._obterTotais(params);

		assertEquals(total, totalEsperado);
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verifyNoMoreInteractions(this.jdbcTemplate);
	}

	@Test
	public void _deveVerificarExistenciaDeRegistrosDadasEstasRestricoes() throws Exception {
		String[] camposParaOrdenacao = null;
		String[] camposSelect = {"id", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
		List<Map<String, Object>> resultadoEsperado = new ArrayList<>();
		Map<String, Object> retorno = new HashMap<>();
		retorno.put("status", "1");
		resultadoEsperado.add(retorno);

		when(this.jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(resultadoEsperado);

		boolean resultado = this.srvDatabase._verificarExistenciaDeRegistrosDadasEstasRestricoes(params);

		assertTrue(resultado);
		verify(this.jdbcTemplate, times(1)).queryForList(Mockito.anyString());
		verifyNoMoreInteractions(this.jdbcTemplate);
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
		
		StringBuilder sql = new StringBuilder("select column_name from information_schema.columns ");
		sql.append("where table_name='").append(tabela).append("'");
		
		when(this.jdbcTemplate.queryForList(sql.toString(), String.class)).thenReturn(new ArrayList<>(camposEsperados));
		
		Set<String> camposTabela = this.srvDatabase._getCamposTabela(tabela, "information_schema.columns");
		
		assertEquals(camposEsperados, camposTabela);
		verify(this.jdbcTemplate, times(1)).queryForList(sql.toString(), String.class);
		verifyNoMoreInteractions(this.jdbcTemplate);
	}
	
	@Test
	public void _deveAtualizarRegistrosEmLote() {
		String camposSelect[] = {"id", "status", "remetente"};
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		Long idRegistro = null;
		
		novosValores.add(new EspecificacaoCampo("status", "15", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "17", "Integer"));

		restricoes.add(new EspecificacaoCampo("id_candidato", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores, restricoes, idRegistro);
		this.srvDatabase._atualizarRegistros(params);

		String[] camposParaOrdenacao = {"id"};
		params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
		
		List<Map<String, Object>> valores = new ArrayList<>();
		Map<String, Object> retorno = new HashMap<>();
		
		retorno.put("id", "3");
		retorno.put("status", "15");
		retorno.put("remetente", "17");
		valores.add(retorno);
		
		retorno = new HashMap<>();
		retorno.put("id", "4");
		retorno.put("status", "15");
		retorno.put("remetente", "17");
		
		valores.add(retorno);
		
		retorno = new HashMap<>();
		retorno.put("id", "5");
		retorno.put("status", "15");
		retorno.put("remetente", "17");
		
		valores.add(retorno);

		when(this.jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(valores);
		
		List<Map<String,String>> registroAlterado = this.srvDatabase._selecioneVariosRegistros(params);
		
		assertEquals(valores, registroAlterado);
		
		verify(this.namedParameterJdbcTemplate, times(1)).update(Mockito.anyString(), Mockito.any(MapSqlParameterSource.class));
		verifyNoMoreInteractions(this.namedParameterJdbcTemplate);
	}

	@SuppressWarnings("unchecked")
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
		
		List<Map<String, Object>> valores = new ArrayList<>();
		Map<String, Object> retorno = new HashMap<>();
		
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
		
		when(this.jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(valores);
		
		params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
		List<Map<String,String>> registroAlterado = this.srvDatabase._selecioneVariosRegistros(params);
		
		assertEquals(valores, registroAlterado);
		verify(this.namedParameterJdbcTemplate, times(1)).batchUpdate(Mockito.anyString(), Mockito.any(Map[].class));
		verifyNoMoreInteractions(this.namedParameterJdbcTemplate);		
	}
}