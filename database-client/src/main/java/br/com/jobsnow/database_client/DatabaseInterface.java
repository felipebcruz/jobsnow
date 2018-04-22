package br.com.jobsnow.database_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database_client.client.Client;
import br.com.jobsnow.database_client.params.EspecificacaoCampo;
import br.com.jobsnow.database_client.params.FuncaoAgregacao;
import br.com.jobsnow.database_client.params.Join;
import br.com.jobsnow.database_client.params.RequestParamsDTO;

public class DatabaseInterface {
	public final String tabela;
	public final Long idRegistro;
	private final Client client;
	private final Set<String> camposTabela;
	private StringBuilder urlDatabase = new StringBuilder("http://localhost:8080/api/v1/database/registros");
	
	public DatabaseInterface(String tabela, Long idRegistro) {
		this.tabela = tabela;
		this.idRegistro = null;
		this.client = new Client();
		
		try {
			this.camposTabela = this._getCampos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
	}
	
	public DatabaseInterface(String tabela) {
		this(tabela, null);
	}
	
	public DatabaseInterface(String tabela, Client client, Set<String> camposTabela) {
		this.tabela = tabela;
		this.idRegistro = null;
		this.client = client;
		this.camposTabela = camposTabela;
	}
	

	public DatabaseInterface(String tabela, Long idRegistro, Client client, Set<String> camposTabela) {
		this.tabela = tabela;
		this.idRegistro = idRegistro;
		this.client = client;
		this.camposTabela = camposTabela;
	}
	
	public List<Map<String, String>> _selecioneVariosRegistros(String[] camposParaSelecionar, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaOrdenacao) {
		this._verificaCamposObrigatorios(camposParaSelecionar);
		
		String url = this.urlDatabase.toString();
		RequestParamsDTO params = new RequestParamsDTO(camposParaSelecionar,this.tabela, joins, restricoes, camposParaOrdenacao);

		InputStream resultado;
		try {
			resultado = this.client._doGet(params, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		List<Map<String, String>> retorno = null;
		String line = "";
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resultado);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<List<Map<String, String>>>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		return retorno;
	}

	public Map<String, String> _selecioneUmUnicoRegistro(String[] camposParaSelecionar, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes){
		boolean idRegistroValido = this.idRegistro != null;

		if(false == idRegistroValido) {
			throw new RuntimeException("Para executar esta tarefa é necessário preencher o idRegistro.");
		}

		this._verificaCamposObrigatorios(camposParaSelecionar);

		String url = this.urlDatabase.append("/").append(this.idRegistro).toString();
		RequestParamsDTO params = new RequestParamsDTO(camposParaSelecionar,this.tabela, joins, restricoes, this.idRegistro,null);
		
		InputStream resultado;
		try {
			resultado = this.client._doGet(params, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		String line = "";
		Map<String, String> retorno = null;
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resultado);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<Map<String, String>>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		return retorno;
	}
	
	public Map<String, Integer> _obterTotais(LinkedHashSet<FuncaoAgregacao> funcoes, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaAgrupamento){
		boolean isTabelaValida = this.tabela != null && this.tabela.length() > 0;

		if(false == isTabelaValida){
			throw new RuntimeException("Para executar esta tarefa é necessário preencher a tabela.");
		} 
		
		this._verificaSeAgregacaoEstaPreenchida(funcoes);
		
		String url = this.urlDatabase.append("/totais").toString();
		RequestParamsDTO params = new RequestParamsDTO(funcoes, this.tabela, joins, restricoes, camposParaAgrupamento);

		InputStream resultado;
		try {
			resultado = this.client._doGet(params, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		String line = "";
		Map<String, Integer> retorno = null;
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resultado);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<Map<String, Integer>>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		return retorno;
	}
	
	public Boolean _verificarExistenciaDeRegistrosDadasEstasRestricoes(LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes){
		String[] camposParaSelecionar = null;
		RequestParamsDTO params = new RequestParamsDTO(camposParaSelecionar,this.tabela, joins, restricoes, null);
		String url = this.urlDatabase.toString();
		
		return this.client._doHead(params, url);
	}
	
	public void _atualizarUmUnicoRegistro(LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores, LinkedHashSet<EspecificacaoCampo> restricoes){
		boolean idRegistroValido = this.idRegistro != null;

		if(false == idRegistroValido) {
			throw new RuntimeException("Para executar esta tarefa é necessário preencher o idRegistro.");
		}
		
		this._verificaCamposObrigatorios(camposMaisSeusNovosValores);
		
		String url = this.urlDatabase.append("/").append(this.idRegistro).toString();
		RequestParamsDTO params = new RequestParamsDTO(this.tabela, camposMaisSeusNovosValores, restricoes, this.idRegistro);
		
		this.client._doPatch(params, url);
	}
	
	public void _alterarStatusDeUmUnicoRegistro(LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores, LinkedHashSet<EspecificacaoCampo> restricoes){
		boolean idRegistroValido = this.idRegistro != null;

		if(false == idRegistroValido) {
			throw new RuntimeException("Para executar esta tarefa é necessário preencher o idRegistro.");
		}

		this._verificaCamposObrigatorios(camposMaisSeusNovosValores);
		
		String url = this.urlDatabase.append("/").append(this.idRegistro).toString();
		RequestParamsDTO params = new RequestParamsDTO(this.tabela, camposMaisSeusNovosValores, restricoes, this.idRegistro);
		
		this.client._doDelete(params, url);
	}

	public Long _inserirUmUnicoRegistro(LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores) throws Exception{
		this._verificaCamposObrigatorios(camposMaisSeusNovosValores);
		
		String url = this.urlDatabase.toString();
		RequestParamsDTO params = new RequestParamsDTO(this.tabela, camposMaisSeusNovosValores);
		
		return this.client._doPost(params, url);
	}

	private void _verificaCamposObrigatorios(String[] camposParaSelecionar) {
		List<String> camposSelect = Arrays.asList(camposParaSelecionar);
		boolean isTabelaValida = this.tabela != null && this.tabela.length() > 0;
		boolean isCamposSelectValido = camposParaSelecionar != null && camposParaSelecionar.length > 0;
		boolean camposParaSelecionarInvalidos = false == this.camposTabela.containsAll(camposSelect);
		
		if(false == isTabelaValida){
			throw new RuntimeException("Para executar esta tarefa é necessário preencher a tabela.");
		} 
		
		if(false == isCamposSelectValido) {
			throw new RuntimeException("Para executar esta tarefa é necessário preencher o camposSelect.");
		}
		
		if (camposParaSelecionarInvalidos) {
			throw new RuntimeException("Os campos do select são inválidos para esta tabela.");
		}
	}
	
	private void _verificaCamposObrigatorios(LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores) {
		List<String> camposSelect = camposMaisSeusNovosValores.stream().map(c -> c.nomeDoCampo).collect(Collectors.toList());
		boolean isTabelaValida = this.tabela != null && this.tabela.length() > 0;
		boolean isNovosValoresValidos = camposMaisSeusNovosValores != null && camposMaisSeusNovosValores.size() > 0;
		boolean camposParaSelecionarInvalidos = false == this.camposTabela.containsAll(camposSelect);
		
		if(false == isTabelaValida){
			throw new RuntimeException("Para executar esta tarefa é necessário preencher a tabela.");
		} 
		
		if(false == isNovosValoresValidos){
			throw new RuntimeException("Para executar esta tarefa é necessário preencher os camposMaisSeusNovosValores.");
		}
		
		if (camposParaSelecionarInvalidos) {
			throw new RuntimeException("Os campos dessa operação são inválidos para esta tabela.");
		}
	}
	
	private void _verificaSeAgregacaoEstaPreenchida(LinkedHashSet<FuncaoAgregacao> funcaoAgregacao) {
		boolean funcoesAgregacaoInvalida = funcaoAgregacao == null || funcaoAgregacao.isEmpty();
		
		if (funcoesAgregacaoInvalida) {
			// TODO
			throw new RuntimeException("");
		}
	}
	
	private Set<String> _getCampos() {
		if (this.tabela == null) {
			// TODO Auto-generated catch block
			throw new RuntimeException("");
		}
		
		boolean empty = this.tabela.isEmpty();

		if(empty) {
			throw new RuntimeException("");
		}
		
		String url = this.urlDatabase.append("/campos").append("/").append(this.tabela).toString();
		
		InputStream resultado = this.client._doGet(url);
		BufferedReader rd = new BufferedReader(new InputStreamReader(resultado));
		String line = "";
		Set<String> camposTabela = null;
		
		try {
			while ((line = rd.readLine()) != null) {
				camposTabela = new ObjectMapper().readValue(line, new TypeReference<Set<String>>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}

		return camposTabela;
	}
}