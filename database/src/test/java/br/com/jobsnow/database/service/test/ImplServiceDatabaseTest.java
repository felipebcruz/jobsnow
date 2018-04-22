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
		this.expectedEx.expectMessage("Os parametros devem estar preenchidos.");
		this.srvDatabase._selecioneVariosRegistros(null);
	}

	@Test
	public void _quandoTabelaEstiverNullEmMontarSQL() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		this.srvDatabase._selecioneVariosRegistros(new DatabaseParamsDTO());
	}

	@Test
	public void _quandoIdDesteRegistroEstiverNullSelecioneUmUnicoRegistro() throws Exception {
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("O id do registro não pode ser null");
		this.srvDatabase._selecioneUmUnicoRegistro(new DatabaseParamsDTO());
	}
	
	@Test
	public void _quandoOsCamposDoUpdateEstiveremNull() throws Exception {
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("Os campos do update devem não podem estar null");
		this.srvDatabase._atualizarUmUnicoRegistro(new DatabaseParamsDTO("teste",null));
	}
	
	@Test
	public void _quandoIdRegistroEstiverNullNoUpdate() throws Exception {
		String tabela = "entrevista";
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("O id do registro a ser atualizado deve ser especificado.");
		this.srvDatabase._atualizarUmUnicoRegistro(new DatabaseParamsDTO(tabela,new LinkedHashSet<>(),null,null));
	}
	
	@Test
	public void _quandoTabelaEstiverNullEmAtualizarUmRegistro() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		this.srvDatabase._atualizarUmUnicoRegistro(new DatabaseParamsDTO(null,null,null,null));
	}
	
	@Test
	public void _quandoCamposDoInsertEstiveremNull() throws Exception {
		this.expectedEx.expect(IllegalArgumentException.class);
		this.expectedEx.expectMessage("O mapa com os valores a serem inseridos não podem estar null");
		this.srvDatabase._inserirUmUnicoRegistro(new DatabaseParamsDTO("teste", null));
	}
	
	@Test
	public void _quandoParamsEstiverNullEmInserirUmUnicoRegistro() throws Exception {
		this.expectedEx.expect(NullPointerException.class);
		this.expectedEx.expectMessage("Para inserir um registro os parametros devem estar preenchidos corretamente.");
		this.srvDatabase._inserirUmUnicoRegistro(null);
	}

	@Test
	public void _quandoTabelaEstiverNullEmInserirUmUnicoRegistro() throws Exception {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		this.srvDatabase._inserirUmUnicoRegistro(new DatabaseParamsDTO(null, new LinkedHashSet<>()));
	}
	
	@Test
	public void _quandoParamsEstiverNullEmAtualizarUmUnicoRegistro() throws Exception {
		this.expectedEx.expect(NullPointerException.class);
		this.expectedEx.expectMessage("Para atualizar um registro os parametros devem estar preenchidos corretamente.");
		this.srvDatabase._atualizarUmUnicoRegistro(null);
	}
	
	@Test
	public void _quandoTabelaEstiverNullEmGetCamposTabela() {
		this.expectedEx.expect(RuntimeException.class);
		this.expectedEx.expectMessage("Para consultar os campos existentes da tabela a tabela precisa estar preenchida.");
		this.srvDatabase._getCamposTabela(null);
	}
	
	@Test
	public void _deveSelecionarVariosRegistros() throws Exception {
		String[] camposSelect = {"id_entrevista", "status", "remetente", "email"};
		String tabela = "entrevista";
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();

		joins.add(new Join("entrevista", "id_candidato", "usuario", "id_usuario"));
		restricoes.add(new EspecificacaoCampo("id_entrevista", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,joins,restricoes,null);

		List<Map<String, Object>> retornoEsperado = new ArrayList<>();
		Map<String, Object> ret = new HashMap<>();
		ret.put("id_entrevista", "2");
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
		StringBuilder sql = new StringBuilder("select kcu.COLUMN_NAME as coluna_pk ")
				.append(" from INFORMATION_SCHEMA.TABLE_CONSTRAINTS as tc ")
				.append(" join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as kcu ")
				.append(" on kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA ")
				.append(" and kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME ")
				.append(" and kcu.TABLE_SCHEMA = tc.TABLE_SCHEMA ")
				.append(" and kcu.TABLE_NAME = tc.TABLE_NAME ")
				.append(" where tc.CONSTRAINT_TYPE = 'PRIMARY KEY' ")
				.append(" and tc.TABLE_NAME = '").append(tabela).append("'");
		
		String[] camposSelect = {"id_entrevista", "status", "remetente", "id_candidato"};
		Long idRegistro = 1L;
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro,null);

		Map<String, Object> esperado = new HashMap<>();
		esperado.put("id_entrevista", "1");
		esperado.put("status", "2");
		esperado.put("remetente", "0");
		esperado.put("id_candidato", "1");

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(esperado);
		when(this.jdbcTemplate.queryForObject(sql.toString(), String.class)).thenReturn(null);
		
		Map<String, String> retorno = this.srvDatabase._selecioneUmUnicoRegistro(params);

		assertEquals(esperado, retorno);
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verify(this.jdbcTemplate, times(1)).queryForObject(sql.toString(), String.class);
		verifyNoMoreInteractions(this.jdbcTemplate);
	}

	@Test
	public void _deveAtualizarUmUnicoRegistro() throws Exception {
		String camposSelect[] = {"status", "remetente"};
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		Long idRegistro = 4L;
		Map<String, Object> valores = new HashMap<>();
		StringBuilder sql = new StringBuilder("select kcu.COLUMN_NAME as coluna_pk ")
				.append(" from INFORMATION_SCHEMA.TABLE_CONSTRAINTS as tc ")
				.append(" join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as kcu ")
				.append(" on kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA ")
				.append(" and kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME ")
				.append(" and kcu.TABLE_SCHEMA = tc.TABLE_SCHEMA ")
				.append(" and kcu.TABLE_NAME = tc.TABLE_NAME ")
				.append(" where tc.CONSTRAINT_TYPE = 'PRIMARY KEY' ")
				.append(" and tc.TABLE_NAME = '").append(tabela).append("'");
		
		valores.put("status", "9");
		valores.put("remetente", "7");
		
		novosValores.add(new EspecificacaoCampo("status", "15", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "17", "Integer"));

		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores, null, idRegistro);

		this.srvDatabase._atualizarUmUnicoRegistro(params);

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(valores);
		when(this.jdbcTemplate.queryForObject(sql.toString(), String.class)).thenReturn(null);
		
		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro,null);

		Map<String, String> registroAlterado = this.srvDatabase._selecioneUmUnicoRegistro(params);

		assertEquals(valores, registroAlterado);
		verify(this.jdbcTemplate, times(1)).update(Mockito.anyString());
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verify(this.jdbcTemplate, times(2)).queryForObject(sql.toString(), String.class);
		verifyNoMoreInteractions(this.jdbcTemplate);
	}

	@Test
	public void _deveAlterarStatusDeUmUnicoRegistro() throws Exception {
		Long idRegistro = 5L;
		String[] camposSelect = {"status"};
		String tabela = "entrevista";
		StringBuilder sql = new StringBuilder("select kcu.COLUMN_NAME as coluna_pk ")
				.append(" from INFORMATION_SCHEMA.TABLE_CONSTRAINTS as tc ")
				.append(" join INFORMATION_SCHEMA.KEY_COLUMN_USAGE as kcu ")
				.append(" on kcu.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA ")
				.append(" and kcu.CONSTRAINT_NAME = tc.CONSTRAINT_NAME ")
				.append(" and kcu.TABLE_SCHEMA = tc.TABLE_SCHEMA ")
				.append(" and kcu.TABLE_NAME = tc.TABLE_NAME ")
				.append(" where tc.CONSTRAINT_TYPE = 'PRIMARY KEY' ")
				.append(" and tc.TABLE_NAME = '").append(tabela).append("'");
		
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		novosValores.add(new EspecificacaoCampo("status", "45", "Integer"));
		
		DatabaseParamsDTO params =  new DatabaseParamsDTO(tabela, novosValores,null,idRegistro);
		
		Map<String, Object> esperado = new HashMap<>();
		esperado.put("status", "45");

		when(this.jdbcTemplate.queryForMap(Mockito.anyString())).thenReturn(esperado);
		when(this.jdbcTemplate.queryForObject(sql.toString(), String.class)).thenReturn(null);

		this.srvDatabase._atualizarUmUnicoRegistro(params);

		params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro,null);
		Map<String, String> registroAlterado = this.srvDatabase._selecioneUmUnicoRegistro(params);

		assertEquals(esperado, registroAlterado);
		verify(this.jdbcTemplate, times(1)).update(Mockito.anyString());
		verify(this.jdbcTemplate, times(1)).queryForMap(Mockito.anyString());
		verify(this.jdbcTemplate, times(2)).queryForObject(sql.toString(), String.class);
		verifyNoMoreInteractions(this.jdbcTemplate);
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
		String[] camposAgrupamento = null;
		LinkedHashSet<FuncaoAgregacao> funcoesAgregacao = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		LinkedHashSet<Join> joins = null;
		
		funcoesAgregacao.add(new FuncaoAgregacao("count", "*"));
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(funcoesAgregacao,tabela,joins,restricoes,camposAgrupamento);
		
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
		String[] camposSelect = {"id_entrevista", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,null);
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
		camposEsperados.add("id_entrevista");
		camposEsperados.add("status");
		camposEsperados.add("remetente");
		camposEsperados.add("id_candidato");
		camposEsperados.add("local");
		camposEsperados.add("data");
		
		StringBuilder sql = new StringBuilder("select column_name from information_schema.columns ");
		sql.append("where table_name='").append(tabela).append("'");
		
		when(this.jdbcTemplate.queryForList(sql.toString(), String.class)).thenReturn(new ArrayList<>(camposEsperados));
		
		Set<String> camposTabela = this.srvDatabase._getCamposTabela(tabela);
		
		assertEquals(camposEsperados, camposTabela);
		verify(this.jdbcTemplate, times(1)).queryForList(sql.toString(), String.class);
		verifyNoMoreInteractions(this.jdbcTemplate);
	}
}