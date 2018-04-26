package com.ccp.jn;

import java.util.ArrayList;
import java.util.HashMap;
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

import br.com.jobsnow.database_client.DatabaseInterface;

@CrossOrigin
@RestController
public class JobsNowRestController {
	
//	@ResponseBody
//	@GetMapping("usuarios/{idDoUsuario}/Candidato/processosSeletivos/antigos/total")
	public Map<String, Integer> totalDeProcessosSeletivosAntigosDesteCandidato(@PathVariable("idDoUsuario") Long idDoUsuario ) {
		
//		select count(*) as total
//		from processos_candidatos pc
//		where pc.status = 0 and pc.idCandidato = :idCandidato
		
		DatabaseInterface tabela = new DatabaseInterface("processos_candidatos");
		Map<String, String> restricoes = new HashMap<>();
//		restricoes.put("nome", "onias");
//		restricoes.put("idade", "32");

		restricoes.put("status", "0");
		restricoes.put("idDoCandidato" ,"" + idDoUsuario);
		Map<String, Integer> totais = tabela._obterTotais(restricoes );
		return totais;
	}
	
	
	@ResponseBody
	@GetMapping("/areasDeAtuacao")
	public List<Map<String, String>> filtragemDeAreaDeAtuacaoParaCadaLetraDigitada(@RequestParam("caracteresDigitados") String caracteresDigitados) {

		DatabaseInterface areasDeAtuacao = new DatabaseInterface("areasDeAtuacao");

		Map<String, String> restricoes = new HashMap<>();
		List<Map<String, String>> resultados = areasDeAtuacao._selecioneVariosRegistros(restricoes, "id", "nome");

		List<Map<String, String>> filtrados = new ArrayList<>();
		
		boolean caracteresInvalidos = caracteresDigitados == null || caracteresDigitados.trim().isEmpty();
		
		if(caracteresInvalidos) {
			return filtrados;
		}
		
		
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
	
	
	@ResponseBody
	@GetMapping("/tiposDeMaoDeObra")
	public List<Map<String, String>> filtragemDeTiposMaoDeObraPorDDDRegiao(@RequestParam("ddd") Integer ddd) {
		
		DatabaseInterface tiposDeMaoDeObra = new DatabaseInterface("tiposDeMaoDeObra");
		
		Map<String, String> restricoes = new HashMap<>();
		
		List<Map<String, String>> resultados = tiposDeMaoDeObra._selecioneVariosRegistros(restricoes, "id", "nome");
		
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
		HashMap<String, String> marcacaoDestaDicaComoLida = new HashMap<>();
		
		marcacaoDestaDicaComoLida.put("remetente", "0");
		marcacaoDestaDicaComoLida.put("idUsuario", "" + idDoUsuario);
		marcacaoDestaDicaComoLida.put("data", "" + System.currentTimeMillis());
		marcacaoDestaDicaComoLida.put("idDica", "" + idDaDica);
		marcacaoDestaDicaComoLida.put("status", "1");
		
		DatabaseInterface dicaUsuario = new DatabaseInterface("dica_usuario");
		
		dicaUsuario._inserirUmUnicoRegistro(marcacaoDestaDicaComoLida);		
	}
	
	
	@DeleteMapping("usuarios/{idDoUsuario}/")
	public void inativacaoDestaContaPorEsteUsuario(@PathVariable("idDoUsuario") Long idUsuario) {
	
		this.desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteEntrevistador(idUsuario);
		this.desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteCandidato(idUsuario);
		this.recusarTodosOsConvitesDeEntrevistasDesteEntrevistador(idUsuario);
		this.cancelarTodosOsProcessosSeletivosDesteEntrevistador(idUsuario);
		this.recusarTodosOsConvitesDeEntrevistasDesteCandidato(idUsuario);
		this.cancelarTodosOsProcessosSeletivosDesteCandidato(idUsuario);
		String nomeTabela = "Usuarios";
		DatabaseInterface tabela = new DatabaseInterface(nomeTabela);	
		tabela._alterarStatusDeUmUnicoRegistro(idUsuario,0L);
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
		
			DatabaseInterface table = new DatabaseInterface("exemplosDeEspecialidade");
			Map<String,String> where = new HashMap<String, String>();
			where.put("idDaAreaDeAtuacao", "" + idDaAreaDeAtuacao);
			where.put("idDaProfissao", "" + idDaProfissao);
			List<Map<String, String>> _selecioneVariosRegistros = table._selecioneVariosRegistros(where, "nome");
			return _selecioneVariosRegistros;
	}
	
	@GetMapping("/areasDeAtuacao/{idDaAreaDeAtuacao}/profissoes/exemplos")
	public List<Map<String, String>> listagemDeExemplosDeProfissoes(@PathVariable("idDaAreaDeAtuacao") Long idDaAreaDeAtuacao){
		
			DatabaseInterface table = new DatabaseInterface("exemplosDeEspecialidade");
			Map<String,String> where = new HashMap<String, String>();
			where.put("idDaAreaDeAtuacao", "" + idDaAreaDeAtuacao);
			List<Map<String, String>> _selecioneVariosRegistros = table._selecioneVariosRegistros(where, "nome");
			return _selecioneVariosRegistros;
	}
	
	
}
