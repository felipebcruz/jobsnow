package br.com.jobsnow.database.resource.test.integration;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import java.util.LinkedHashSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database.DatabaseApplication;
import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.params.EspecificacaoCampo;
import br.com.jobsnow.database.params.FuncaoAgregacao;
import br.com.jobsnow.database.params.Join;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DatabaseApplication.class)
@WebAppConfiguration
public class DatabaseResourceIntegrationTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private StringBuilder url = new StringBuilder("/api/v1/database/registros");

	@Before
	public void _setUp() throws Exception {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
	}
	
	@Test
	public void _deveSelecionarVariosRegistros() throws Exception {
		String[] camposParaOrdenacao = null;
		String[] camposSelect = {"id", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		LinkedHashSet<Join> joins = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();

		restricoes.add(new EspecificacaoCampo("id", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,joins,restricoes,camposParaOrdenacao);
		
		this.mockMvc.perform(get(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$", hasSize(1)))
	            .andExpect(jsonPath("$[0].id", is("2")))
	            .andExpect(jsonPath("$[0].status", is("2")))
	            .andExpect(jsonPath("$[0].remetente", is("0")))
	            .andExpect(jsonPath("$[0].id_candidato", is("1")))
				.andExpect(content().contentType(this.contentType)).andReturn();
	}

	@Test
	public void _deveSelecionarUmRegistro() throws Exception {
		this.url.append("/{id}");
		String[] camposSelect = {"id", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		Long idRegistro = 1L;
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,null,idRegistro);
		
		this.mockMvc.perform(get(this.url.toString(),idRegistro)
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$.id", is("1")))
	            .andExpect(jsonPath("$.status", is("2")))
	            .andExpect(jsonPath("$.remetente", is("0")))
	            .andExpect(jsonPath("$.id_candidato", is("1")))
				.andExpect(content().contentType(this.contentType)).andReturn();
	}

	@Test
	public void _deveObterTotais() throws Exception {
		this.url.append("/totais");
		
		String tabela = "entrevista";
		String[] camposParaSelecionar = null;
		String[] camposAgrupamento = null;
		LinkedHashSet<FuncaoAgregacao> funcoesAgregacao = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		LinkedHashSet<Join> joins = null;
		
		funcoesAgregacao.add(new FuncaoAgregacao("count", "*"));
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposParaSelecionar,funcoesAgregacao,tabela,joins,restricoes,camposAgrupamento);
		
		this.mockMvc.perform(get(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isOk())
	            .andExpect(jsonPath("$.count", is("2")))
				.andExpect(content().contentType(this.contentType)).andReturn();		
	}

	@Test
	public void _deveAtualizarUmUnicoRegistro() throws Exception {
		this.url.append("/{id}");
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		Long idRegistro = 4L;
		
		novosValores.add(new EspecificacaoCampo("status", "15", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "17", "Integer"));

		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores, null, idRegistro);
				
		this.mockMvc.perform(patch(this.url.toString(), idRegistro)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isNoContent()).andReturn();
	}

	@Test
	public void _deveAlterarStatusDeUmUnicoRegistro() throws Exception {
		this.url.append("/{id}");
		Long idRegistro = 5L;
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		novosValores.add(new EspecificacaoCampo("status", "45", "Integer"));
		
		DatabaseParamsDTO params =  new DatabaseParamsDTO(tabela, novosValores,null,idRegistro);
		
		this.mockMvc.perform(delete(this.url.toString(), idRegistro)
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isNoContent()).andReturn();
	}

	@Ignore
	@Test
	public void _deveInserirUmUnicoRegistro() throws Exception {
		String urlObjetoCriado = "/api/v1/database/registros/6";
		
		String tabela = "entrevista";
		LinkedHashSet<EspecificacaoCampo> novosValores = new LinkedHashSet<>();
		
		novosValores.add(new EspecificacaoCampo("id", "6", "Integer"));
		novosValores.add(new EspecificacaoCampo("status", "11", "Integer"));
		novosValores.add(new EspecificacaoCampo("remetente", "3", "Integer"));
		novosValores.add(new EspecificacaoCampo("id_candidato", "1", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(tabela, novosValores);
		
		this.mockMvc.perform(post(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.content(new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString(urlObjetoCriado)))
				.andReturn();
	}

	@Test
	public void _deveVerificarExistenciaDeRegistrosDadasEstasRestricoes() throws Exception{
		String[] camposParaOrdenacao = null;
		String[] camposSelect = {"id", "status", "remetente", "id_candidato"};
		String tabela = "entrevista";
		
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("status", "2", "Integer"));
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(camposSelect,tabela,null,restricoes,camposParaOrdenacao);
				
		this.mockMvc.perform(head(this.url.toString())
				.accept(this.contentType)
				.contentType(this.contentType)
				.header("params", new ObjectMapper().writeValueAsString(params)))
				.andExpect(status().isOk())
				.andReturn();
	}
}