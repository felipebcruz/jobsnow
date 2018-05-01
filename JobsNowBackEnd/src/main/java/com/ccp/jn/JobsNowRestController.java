package com.ccp.jn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.jobsnow.database.api.Campos;
import br.com.jobsnow.database.api.JobsNowDataBaseClient;
import br.com.jobsnow.database.client.params.Sumarizacao;

@CrossOrigin
@RestController
public class JobsNowRestController {
	
//	@ResponseBody
//	@GetMapping("usuarios/{idDoUsuario}/Candidato/processosSeletivos/antigos/total")
	public Map<String, BigDecimal> totalDeProcessosSeletivosAntigosDesteCandidato(@PathVariable("idDoUsuario") Long idDoUsuario ) {
		
//		select count(*) as total
//		from processos_candidatos pc
//		where pc.status = 0 and pc.idCandidato = :idCandidato
		
//		DatabaseInterface tabela = new DatabaseInterface("processos_candidatos");
//		Map<String, String> restricoes = new HashMap<>();
//
//		restricoes.put("status", "0");
//		restricoes.put("idDoCandidato" ,"" + idDoUsuario);
//		Map<String, Integer> totais = tabela._obterTotais(restricoes );
//		return totais;
		
		Map<String, BigDecimal> valores = JobsNowDataBaseClient
			._dentroDaTabela("processos_candidatos")
			._facaUmaSelecaoDeRegistros()
			._comBaseNestesCriteriosDeSelecaoDeRegistros(
					Campos
					._adicioneEsteCampoComEsteValor("status", "0")
					._depoisAdicioneEsteCampoComEsteValor("idDoCandidato" ,"" + idDoUsuario)
					._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados()
			)
			._paraEntaoSumarizarValores(new Sumarizacao("count", ""));
		
		return valores;
	}
	
	
	@ResponseBody
	@GetMapping("/areasDeAtuacao")
	public List<Map<String, String>> filtragemDeAreaDeAtuacaoParaCadaLetraDigitada(@RequestParam("caracteresDigitados") String caracteresDigitados) {

//		DatabaseInterface areasDeAtuacao = new DatabaseInterface("areasDeAtuacao");
//
//		Map<String, String> restricoes = new HashMap<>();
//		List<Map<String, String>> resultados = areasDeAtuacao._selecioneVariosRegistros(restricoes, "id", "nome");

		List<Map<String, String>> filtrados = new ArrayList<>();
		
		boolean caracteresInvalidos = caracteresDigitados == null || caracteresDigitados.trim().isEmpty();
		
		if(caracteresInvalidos) {
			return filtrados;
		}
		
		
		String nomeDaTabela = "areasDeAtuacao";
	
		List<Map<String, String>> resultados = this.obterTodosOsIdsMaisNomesDestaTabela(nomeDaTabela);
		
		for (Map<String, String> resultado : resultados) {

			boolean naoContemONome = false == resultado.containsKey("nome");
		
			if (naoContemONome) {
				continue;
			}

			boolean naoContemOId = false == resultado.containsKey("id");
			
			if (naoContemOId) {
				continue;
			}

			String id = resultado.get("id");
			String nome = resultado.get("nome");
			
			boolean nomeInvalido = nome == null || nome.trim().isEmpty();
			
			if(nomeInvalido) {
				continue;
			}

			boolean naoEhEsteNome = false == nome.toUpperCase().startsWith(caracteresDigitados.toUpperCase());
			
			if(naoEhEsteNome) {
				continue;
			}
			
			Map<String, String> filtrado = new HashMap<>();
			filtrado.put("nome", nome);
			filtrado.put("id", id);
			filtrados.add(filtrado);
		}

		return filtrados;
	}


	private List<Map<String, String>> obterTodosOsIdsMaisNomesDestaTabela(String nomeDaTabela) {
		List<Map<String, String>> resultados = JobsNowDataBaseClient
			._dentroDaTabela(nomeDaTabela)
			._facaUmaSelecaoDeRegistros()
			._tragaEstasColunas(Campos
					._adicioneEsteCampoComEsteValor("id", "")
					._depoisAdicioneEsteCampoComEsteValor("Nome", "")
					._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados()
					)
			._deUmConjuntoDeRegistrosQueAtendemDeterminadosCriteriosDeSelecao(new HashSet<>());
		return resultados;
	}
	
	
	@ResponseBody
	@GetMapping("/tiposDeMaoDeObra")
	public List<Map<String, String>> filtragemDeTiposMaoDeObraPorDDDRegiao(@RequestParam("ddd") Integer ddd) {
		
//		DatabaseInterface tiposDeMaoDeObra = new DatabaseInterface("tiposDeMaoDeObra");
//		
//		Map<String, String> restricoes = new HashMap<>();
//		
//		List<Map<String, String>> resultados = tiposDeMaoDeObra._selecioneVariosRegistros(restricoes, "id", "nome");

		List<Map<String, String>> resultados = this.obterTodosOsIdsMaisNomesDestaTabela("tiposDeMaoDeObra");
		
		List<Map<String, String>> filtrados = new ArrayList<>();
		
		for (Map<String, String> resultado : resultados) {
			
			boolean naoContenONome = false == resultado.containsKey("nome");
			if(naoContenONome) {
				continue;
			}
			
			boolean naoContemOId = false == resultado.containsKey("id");
			
			if(naoContemOId) {
				continue;
			}
			
			String id = resultado.get("id");
			String nome = resultado.get("nome");
			
			
			boolean nomeInvalido = nome == null || nome.trim().isEmpty();
			
			if(nomeInvalido) {
				continue;
			}
			
			String strDDD = resultado.get("ddd");
			
			Integer dddFound;
			
			
			try {
				
				dddFound = new Integer(strDDD);
				
			} catch (RuntimeException e) {
				
				continue;
			}
			
			boolean naoEhOMesmoDDD = false == dddFound.equals(ddd);
			
			if(naoEhOMesmoDDD) {
				
				continue;
			}
			
			Map<String, String> filtrado = new HashMap<>();
			filtrado.put("nome", nome);
			filtrado.put("id", id);
			filtrados.add(filtrado);			
		}
		return filtrados;
		
	}
	
	
	
	@ResponseBody
	@PostMapping("usuarios/{idDoUsuario}/dicasProfissionais/NaoLidas/{idDaDica}")
	public void marcacaoDestaDicaComoLidaPorEsteUsusario(@PathVariable("idDoUsuario") Long idDoUsuario, @PathVariable("idDaDica") Long idDaDica) {
		
		//insert into dica_usuario (remetente, idUsuario, data, idDica, status) values (0, :idUsuario, getDate(), :idDica, 1)
//		HashMap<String, String> marcacaoDestaDicaComoLida = new HashMap<>();
//		
//		marcacaoDestaDicaComoLida.put("remetente", "0");
//		marcacaoDestaDicaComoLida.put("idUsuario", "" + idDoUsuario);
//		marcacaoDestaDicaComoLida.put("data", "" + System.currentTimeMillis());
//		marcacaoDestaDicaComoLida.put("idDica", "" + idDaDica);
//		marcacaoDestaDicaComoLida.put("status", "1");
//		
//		DatabaseInterface dicaUsuario = new DatabaseInterface("dicas_usuarios");
//		
//		dicaUsuario._inserirUmUnicoRegistro(marcacaoDestaDicaComoLida);		

	JobsNowDataBaseClient
		._dentroDaTabela("dicas_usuarios")
		._salveOsValores(
				Campos
					._adicioneEsteCampoComEsteValor("remetente", "0")
					._depoisAdicioneEsteCampoComEsteValor("idUsuario", "" + idDoUsuario)
					._depoisAdicioneEsteCampoComEsteValor("data", "" + System.currentTimeMillis())
					._depoisAdicioneEsteCampoComEsteValor("idDica", "" + idDaDica)
					._depoisAdicioneEsteCampoComEsteValor("status", "1")
					._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados()
		)
		._paraInserirUmNovoRegistro();
	
	;
	}
	
	
	@DeleteMapping("usuarios/{idDoUsuario}/")
	public void inativacaoDestaContaPorEsteUsuario(@PathVariable("idDoUsuario") Long idUsuario) {
	
		this.desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteEntrevistador(idUsuario);
		this.desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteCandidato(idUsuario);
		this.recusarTodosOsConvitesDeEntrevistasDesteEntrevistador(idUsuario);
		this.cancelarTodosOsProcessosSeletivosDesteEntrevistador(idUsuario);
		this.recusarTodosOsConvitesDeEntrevistasDesteCandidato(idUsuario);
		this.cancelarTodosOsProcessosSeletivosDesteCandidato(idUsuario);
//		String nomeTabela = "Usuarios";
//		DatabaseInterface tabela = new DatabaseInterface(nomeTabela);	
//		tabela._alterarStatusDeUmUnicoRegistro(idUsuario,0L);
		
		JobsNowDataBaseClient
			._dentroDaTabela("Usuarios")
			._altereStatus(0L)
			._deUmUnicoRegistro(idUsuario)
		;
	
	}

	private void desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteEntrevistador(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	private void desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteCandidato(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	private void recusarTodosOsConvitesDeEntrevistasDesteCandidato(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	private void recusarTodosOsConvitesDeEntrevistasDesteEntrevistador(Long idUsuario) {
		JobsNowRequest request = new JobsNowRequest("usuarios/{idDoUsuario}/entrevistador/entrevistas/cancelamento");
		Map<String, String> pathVariables = new HashMap<>();
		pathVariables.put("idDoUsuario", "" + idUsuario);
		request._head(pathVariables);
		
		
	}

	private void cancelarTodosOsProcessosSeletivosDesteCandidato(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	private void cancelarTodosOsProcessosSeletivosDesteEntrevistador(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}
	
	
	@GetMapping("/areasDeAtuacao/{idDaAreaDeAtuacao}/profissoes/{idDaProfissao}/especialidades/exemplos")
	public List<Map<String, String>> listagemDeExemplosDeEspecialidades(@PathVariable("idDaAreaDeAtuacao") Long idDaAreaDeAtuacao, @PathVariable("idDaProfissao") Long idDaProfissao){
		
//			DatabaseInterface table = new DatabaseInterface("exemplosDeEspecialidade");
//			Map<String,String> where = new HashMap<String, String>();
//			where.put("idDaAreaDeAtuacao", "" + idDaAreaDeAtuacao);
//			where.put("idDaProfissao", "" + idDaProfissao);
//			List<Map<String, String>> _selecioneVariosRegistros = table._selecioneVariosRegistros(where, "nome");
//			return _selecioneVariosRegistros;

		List<Map<String, String>> resultado = JobsNowDataBaseClient
			._dentroDaTabela("exemplosDeEspecialidade")
			._facaUmaSelecaoDeRegistros()
			._tragaEstasColunas(Campos
					._adicioneEsteCampoComEsteValor("nome", "")
					._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados()
					)
			._deUmConjuntoDeRegistrosQueAtendemDeterminadosCriteriosDeSelecao(Campos
						._adicioneEsteCampoComEsteValor("idDaAreaDeAtuacao", "" + idDaAreaDeAtuacao)
						._depoisAdicioneEsteCampoComEsteValor("idDaProfissao", "" + idDaProfissao)
						._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados());
		
		return resultado;
	
	}
	
	@GetMapping("/areasDeAtuacao/{idDaAreaDeAtuacao}/profissoes/exemplos")
	public List<Map<String, String>> listagemDeExemplosDeProfissoes(@PathVariable("idDaAreaDeAtuacao") Long idDaAreaDeAtuacao){
		
//			DatabaseInterface table = new DatabaseInterface("exemplosDeEspecialidade");
//			Map<String,String> where = new HashMap<String, String>();
//			where.put("idDaAreaDeAtuacao", "" + idDaAreaDeAtuacao);
//			List<Map<String, String>> _selecioneVariosRegistros = table._selecioneVariosRegistros(where, "nome");
//			return _selecioneVariosRegistros;
		List<Map<String, String>> resultado = JobsNowDataBaseClient
				._dentroDaTabela("exemplosDeProfissoes")
				._facaUmaSelecaoDeRegistros()
				._tragaEstasColunas(Campos
						._adicioneEsteCampoComEsteValor("nome", "")
						._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados()
						)
				._deUmConjuntoDeRegistrosQueAtendemDeterminadosCriteriosDeSelecao(Campos
							._adicioneEsteCampoComEsteValor("idDaAreaDeAtuacao", "" + idDaAreaDeAtuacao)
							._porFimRetorneTodosOsCamposNaOrdemQueForamAdicionados());
			
			return resultado;
	}
	
	
}
