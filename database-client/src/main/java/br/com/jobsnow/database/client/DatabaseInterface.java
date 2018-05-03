package br.com.jobsnow.database.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database.api.EspecificacaoCampo;
import br.com.jobsnow.database.api.Join;
import br.com.jobsnow.database.client.params.RequestParamsDTO;
import br.com.jobsnow.database.client.params.Sumarizacao;

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
		
		this.camposTabela = this._getCampos();
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
	
	public List<Map<String, String>> _selecioneVariosRegistros(Map<String, String> restricoes, String...colunasParaTrazer){
		
		boolean naoFoiEspecificadoQuaisColunasTrazer = colunasParaTrazer == null || colunasParaTrazer.length == 0;
		
		if(naoFoiEspecificadoQuaisColunasTrazer) {
			throw new CamposDoSelectNaoForamEspecificadosException();
		}
		
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restrictions = this.adequarRestricoes(restricoes);
		List<Map<String, String>> _selecioneVariosRegistros = this._selecioneVariosRegistros(colunasParaTrazer, joins, restrictions, colunasParaTrazer[0]);
		return _selecioneVariosRegistros;
	}
	
	public List<Map<String, String>> _selecioneVariosRegistros(String[] camposParaSelecionar, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaOrdenacao) {
		this._verificaCamposObrigatorios(camposParaSelecionar);
		
		String url = this.urlDatabase.toString();
		RequestParamsDTO params = new RequestParamsDTO(camposParaSelecionar,this.tabela, joins, restricoes, camposParaOrdenacao);

		InputStream resultado = this.client._doGet(params, url);
		
		List<Map<String, String>> retorno = null;
		String line = "";
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resultado);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<List<Map<String, String>>>(){});
			}
		}  catch (Exception e) {
			throw new RuntimeException(e);
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
		
		InputStream resultado = this.client._doGet(params, url);
		
		String line = "";
		Map<String, String> retorno = null;
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resultado);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<Map<String, String>>(){});
			}
		}  catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return retorno;
	}
	
	public Map<String, Long> _obterTotais(Map<String, String> restricoes){
		
		LinkedHashSet<EspecificacaoCampo> restricions = this.adequarRestricoes(restricoes);
		LinkedHashSet<Sumarizacao> funcoes = this.obterFuncaoCount();
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		
		Map<String, Long> _obterTotais = this._obterTotais(funcoes, joins, restricions);
		return _obterTotais;
	}

	private LinkedHashSet<Sumarizacao> obterFuncaoCount() {
		LinkedHashSet<Sumarizacao> funcoes = new LinkedHashSet<>();
		Sumarizacao count = new Sumarizacao("Count", "id");
		funcoes.add(count);
		return funcoes;
	}

	private LinkedHashSet<EspecificacaoCampo> adequarRestricoes(Map<String, String> restricoes) {
		LinkedHashSet<EspecificacaoCampo> restricions = new LinkedHashSet<>();
		Set<String> keySet = restricoes.keySet();
		for (String nomeDoCampo : keySet) {
			String valorDoCampo = restricoes.get(nomeDoCampo);
			EspecificacaoCampo restriction  = new EspecificacaoCampo(nomeDoCampo, valorDoCampo);
			restricions.add(restriction);
		}
		return restricions;
	}
	
	public Map<String, Long> _obterTotais(LinkedHashSet<Sumarizacao> funcoes, LinkedHashSet<Join> joins, LinkedHashSet<EspecificacaoCampo> restricoes, String... camposParaAgrupamento){
		boolean isTabelaValida = this.tabela != null && this.tabela.length() > 0;

		if(false == isTabelaValida){
			throw new RuntimeException("Para executar esta tarefa é necessário preencher a tabela.");
		} 
		
		this._verificaSeAgregacaoEstaPreenchida(funcoes);
		
		String url = this.urlDatabase.append("/totais").toString();
		RequestParamsDTO params = new RequestParamsDTO(funcoes, this.tabela, joins, restricoes, camposParaAgrupamento);

		InputStream resultado = this.client._doGet(params, url);
		
		String line = "";
		Map<String, Long> retorno = null;
		
		try (
			InputStreamReader inputStreamReader = new InputStreamReader(resultado);
			BufferedReader rd = new BufferedReader(inputStreamReader);
		){
			// Felipe, esse loop só passa uma vez?
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<Map<String, Long>>(){});
			}
		}  catch (Exception e) {
			throw new RuntimeException(e);
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

	
	public void _alterarStatusDeUmUnicoRegistro(Long novoValorStatus, Long id) {
		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = new LinkedHashSet<>();
		EspecificacaoCampo valor = new EspecificacaoCampo("status", novoValorStatus.toString());
		camposMaisSeusNovosValores.add(valor);
		String url = this.urlDatabase.append("/").append(this.idRegistro).toString();
		RequestParamsDTO params = new RequestParamsDTO(this.tabela, camposMaisSeusNovosValores, new LinkedHashSet<>(), this.idRegistro);
		
		this.client._doDelete(params, url);
	}
	
	public void _alterarStatusDeUmUnicoRegistro(LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores, LinkedHashSet<EspecificacaoCampo> restricoes){

		this._verificaCamposObrigatorios(camposMaisSeusNovosValores);
		
		String url = this.urlDatabase.append("/").append(this.idRegistro).toString();
		RequestParamsDTO params = new RequestParamsDTO(this.tabela, camposMaisSeusNovosValores, restricoes, this.idRegistro);
		
		this.client._doDelete(params, url);
	}

	public Long _inserirUmUnicoRegistro(Map<String, String> valores){
		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = this.adequarRestricoes(valores);
		
		Long _inserirUmUnicoRegistro = this._inserirUmUnicoRegistro(camposMaisSeusNovosValores);
		return _inserirUmUnicoRegistro;
	}	
	
	public Long _inserirUmUnicoRegistro(LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores){
		this._verificaCamposObrigatorios(camposMaisSeusNovosValores);
		
		String url = this.urlDatabase.toString();
		RequestParamsDTO params = new RequestParamsDTO(this.tabela, camposMaisSeusNovosValores);
		
		Long _doPost = this.client._doPost(params, url);
		return _doPost;
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
	
	private void _verificaSeAgregacaoEstaPreenchida(LinkedHashSet<Sumarizacao> funcaoAgregacao) {

		boolean funcoesAgregacaoInvalida = funcaoAgregacao == null || funcaoAgregacao.isEmpty();
		
		if (funcoesAgregacaoInvalida) {
			throw new RuntimeException("Especifique ao menos uma função de agregação");
		}
	}
	
	private Set<String> _getCampos() {


		String url = this.urlDatabase.append("/campos").append("/").append(this.tabela).toString();
		
		InputStream resultado = this.client._doGet(url);
		BufferedReader rd = new BufferedReader(new InputStreamReader(resultado));
		String line = "";
		Set<String> camposTabela = null;
		
		try {
			while ((line = rd.readLine()) != null) {
				camposTabela = new ObjectMapper().readValue(line, new TypeReference<Set<String>>(){});
			}
		}  catch (Exception e) {
			throw new RuntimeException(e);
		}

		return camposTabela;
	}
}