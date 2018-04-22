package br.com.jobsnow.database.resource.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database.DatabaseApplication;
import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.service.ServiceDatabase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseApplication.class)
@WebAppConfiguration
public class DatabaseResourceTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;
	private DatabaseParamsDTO databaseParamsDTO = new DatabaseParamsDTO();

	@MockBean
	private ServiceDatabase srvDatabase;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private StringBuilder url = new StringBuilder("/api/v1/database/registros");

	@Before
	public void _setUp() throws Exception {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
	}
	
	@Test
	public void _naoPodeSelecionarVariosRegistrosSemParams() throws Exception {
		this.mockMvc.perform(get(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("O header params deve ser preenchido.")));
	}
	
	@Test
	public void _naoPodeSelecionarVariosRegistrosSemTabela() throws Exception {
		doThrow(BadSqlGrammarException.class).when(this.srvDatabase)._selecioneVariosRegistros(Mockito.any(DatabaseParamsDTO.class));
		
		this.mockMvc.perform(get(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("O sql descrito no parametro e invalido")));
		
		verify(this.srvDatabase, times(1))._selecioneVariosRegistros(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}
	
	@Test
	public void _naoPodeSelecionarUmUnicoRegistroSemParamsPreenchido() throws Exception {
		this.url.append("/{id}");
		this.mockMvc.perform(get(this.url.toString(),1L)
				.accept(this.contentType)
				.contentType(this.contentType))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("O header params deve ser preenchido.")));
	}
	
	@Test
	public void _naoPodeInserirUmRegistroSemOsCamposPreenchidos() throws Exception {
		doThrow(new NullPointerException("O mapa com os valores a serem inseridos n�o podem estar null"))
			.when(this.srvDatabase)._inserirUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		
		this.mockMvc.perform(post(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("O mapa com os valores a serem inseridos n�o podem estar null")));
		
		verify(this.srvDatabase, times(1))._inserirUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}
	
	@Test
	public void _naoPodeInserirUmRegistroSemTabela() throws Exception {
		doThrow(BadSqlGrammarException.class).when(this.srvDatabase)._inserirUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		
		this.mockMvc.perform(post(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("O sql descrito no parametro e invalido")));
		
		verify(this.srvDatabase, times(1))._inserirUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}
	
	@Test
	public void _naoPodeAtualizarUmRegistroSemTabela() throws Exception {
		doThrow(BadSqlGrammarException.class).when(this.srvDatabase)._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		
		this.url.append("/{id}");
		this.mockMvc.perform(patch(this.url.toString(), 1L)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("O sql descrito no parametro e invalido")));
		
		verify(this.srvDatabase, times(1))._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}
	
	@Test
	public void _naoPodeAtualizarUmUnicoRegistroSemCamposPreenchido() throws Exception {
		this.url.append("/{id}");
		
		doThrow(new IllegalArgumentException("Os campos do update devem n�o podem estar null"))
			.when(this.srvDatabase)._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		
		this.mockMvc.perform(patch(this.url.toString(),1L)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Os campos do update devem n�o podem estar null")));
		
		verify(this.srvDatabase, times(1))._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}
	
	@Test
	public void _naoPodeAlterarStatusUmUnicoRegistroSemCamposPreenchido() throws Exception {
		this.url.append("/{id}");
		
		doThrow(new IllegalArgumentException("Os campos do update devem n�o podem estar null"))
			.when(this.srvDatabase)._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		
		this.mockMvc.perform(delete(this.url.toString(),1L)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Os campos do update devem n�o podem estar null")));
		
		verify(this.srvDatabase, times(1))._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}
	
	@Test
	public void _naoPodeAlterarStatusUmRegistroSemTabela() throws Exception {
		doThrow(BadSqlGrammarException.class).when(this.srvDatabase)._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		
		this.url.append("/{id}");
		this.mockMvc.perform(delete(this.url.toString(), 1L)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("O sql descrito no parametro e invalido")));
		
		verify(this.srvDatabase, times(1))._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}
	
	@Test
	public void _deveSelecionarVariosRegistros() throws Exception {
		List<Map<String, String>> retornoEsperado = new ArrayList<>();
		Map<String, String> ret = new HashMap<>();
		ret.put("id_entrevista", "2");
		ret.put("status", "2");
		ret.put("remetente", "0");
		ret.put("id_candidato", "1");
		retornoEsperado.add(ret);
		
		ret = new HashMap<>();
		ret.put("id_entrevista", "3");
		ret.put("status", "10");
		ret.put("remetente", "8");
		ret.put("id_candidato", "1");
		retornoEsperado.add(ret);
		
		when(this.srvDatabase._selecioneVariosRegistros(Mockito.any(DatabaseParamsDTO.class))).thenReturn(retornoEsperado);

		this.mockMvc.perform(get(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(2)))
	            .andExpect(jsonPath("$[0].id_entrevista", is("2")))
	            .andExpect(jsonPath("$[0].status", is("2")))
	            .andExpect(jsonPath("$[0].remetente", is("0")))
	            .andExpect(jsonPath("$[0].id_candidato", is("1")))
	            .andExpect(jsonPath("$[1].id_entrevista", is("3")))
	            .andExpect(jsonPath("$[1].status", is("10")))
	            .andExpect(jsonPath("$[1].remetente", is("8")))
	            .andExpect(jsonPath("$[1].id_candidato", is("1")))
				.andExpect(content().contentType(this.contentType)).andReturn();

		verify(this.srvDatabase, times(1))._selecioneVariosRegistros(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}

	
	@Test
	public void _deveSelecionarUmRegistro() throws Exception {
		this.url.append("/{id}");
		Long idDesteRegistro = 1L;
		Map<String, String> retornoEsperado = new HashMap<>();
		retornoEsperado.put("id_entrevista", "2");
		retornoEsperado.put("status", "2");
		retornoEsperado.put("remetente", "0");
		retornoEsperado.put("id_candidato", "1");
		
		when(this.srvDatabase._selecioneUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class))).thenReturn(retornoEsperado);
		
		this.mockMvc.perform(get(this.url.toString(),idDesteRegistro)
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$.id_entrevista", is("2")))
	            .andExpect(jsonPath("$.status", is("2")))
	            .andExpect(jsonPath("$.remetente", is("0")))
	            .andExpect(jsonPath("$.id_candidato", is("1")))
				.andExpect(content().contentType(this.contentType)).andReturn();

		verify(this.srvDatabase, times(1))._selecioneUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase); 
	}
	
	@Test
	public void _deveObterTotais() throws Exception {
		this.url.append("/totais");
		Map<String, String> totalEsperado = new HashMap<>();
		totalEsperado.put("total", "3");
		
		when(this.srvDatabase._obterTotais(Mockito.any(DatabaseParamsDTO.class))).thenReturn(totalEsperado);
		
		this.mockMvc.perform(get(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$.total", is("3")))
				.andExpect(content().contentType(this.contentType)).andReturn();
		
		verify(this.srvDatabase, times(1))._obterTotais(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}
	
	@Test
	public void _deveAtualizarUmUnicoRegistro() throws Exception {
		this.url.append("/{id}");
		Long idDesteRegistro = 4L;
		Map<String, Object> camposMaisSeusNovosValores = new HashMap<>();
		
		camposMaisSeusNovosValores.put("status", 10);
		camposMaisSeusNovosValores.put("remetente", 15);
		
		this.mockMvc.perform(patch(this.url.toString(), idDesteRegistro)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO))) 
				.andExpect(status().isNoContent()).andReturn();
		
		verify(this.srvDatabase, times(1))._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}

	@Test
	public void _deveAlterarStatusDeUmUnicoRegistro() throws Exception {
		this.url.append("/{id}");
		Long idDesteRegistro = 4L;
		
		this.mockMvc.perform(delete(this.url.toString(), idDesteRegistro)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isNoContent()).andReturn();
		
		verify(this.srvDatabase, times(1))._atualizarUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}
	
	@Test
	public void _deveInserirUmUnicoRegistro() throws Exception {
		Map<String, Object> novosValores = new HashMap<>();
		String urlObjetoCriado = "/api/v1/database/registros/5";
		
		novosValores.put("id_entrevista", 5);
		novosValores.put("status", 3);
		novosValores.put("remetente", 4);
		novosValores.put("id_candidato", 1);
		
		when(this.srvDatabase._inserirUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class))).thenReturn(5L);
		
		this.mockMvc.perform(post(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString(urlObjetoCriado)))
				.andReturn();
		
		verify(this.srvDatabase, times(1))._inserirUmUnicoRegistro(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}
	
	@Test
	public void _deveVerificarExistenciaDeRegistrosDadasEstasRestricoes() throws Exception{
		Map<String, Object> restricoes = new HashMap<>();
		restricoes.put("where id_entrevista > ", 1);
		
		when(this.srvDatabase._verificarExistenciaDeRegistrosDadasEstasRestricoes(Mockito.any(DatabaseParamsDTO.class))).thenReturn(true);
		
		this.mockMvc.perform(head(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(this.databaseParamsDTO)))
				.andExpect(status().isOk())
				.andReturn();
		
		verify(this.srvDatabase, times(1))._verificarExistenciaDeRegistrosDadasEstasRestricoes(Mockito.any(DatabaseParamsDTO.class));
		verifyNoMoreInteractions(this.srvDatabase);
	}
}