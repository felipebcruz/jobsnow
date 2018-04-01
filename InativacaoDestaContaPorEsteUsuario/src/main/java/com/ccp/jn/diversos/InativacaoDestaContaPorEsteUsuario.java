package com.ccp.jn.diversos;

//Próxima atividade: fazer as quatro anotações necessárias 

import com.ccp.bancoDeDados.restInterface.Tabela;

public class InativacaoDestaContaPorEsteUsuario {
	
	public void executar(Long idUsuario) {
		this.desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteEntrevistador(idUsuario);
		this.desistirDeTodosOsConvitesDeEntrevistasEnviadosPorEsteCandidato(idUsuario);
		this.recusarTodosOsConvitesDeEntrevistasDesteEntrevistador(idUsuario);
		this.cancelarTodosOsProcessosSeletivosDesteEntrevistador(idUsuario);
		this.recusarTodosOsConvitesDeEntrevistasDesteCandidato(idUsuario);
		this.cancelarTodosOsProcessosSeletivosDesteCandidato(idUsuario);
		String nomeTabela = "Usuarios";
		Tabela tabela = new Tabela(nomeTabela);	
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
		// TODO Auto-generated method stub
		
	}

	private void cancelarTodosOsProcessosSeletivosDesteCandidato(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	private void cancelarTodosOsProcessosSeletivosDesteEntrevistador(Long idUsuario) {
		// TODO Auto-generated method stub
		
	}
	
}
