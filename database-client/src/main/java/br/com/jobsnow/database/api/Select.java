package br.com.jobsnow.database.api;

public class Select {

	 
	
}


class Campo{
	/*
	 * na tabela(nomeDaTabela)
	 *--salveOsValores(camposMaisValores)[ comoAlteracao()[deUmUnicoRegistro(id) ou  deUmConjuntoDeRegistros(condicoes)] ou deUmNovoRegistro() ]
	 *-- crieVariosRegistrosDeUmaUnicaVez(camposMaisValores[])
	 * --altereStatus(status) [deUmUnicoRegistro(id) ou deUmConjuntoDeRegistros(condicoes)]
	 * facaBuscaNoBancoDeDadosPara(), retornarInformacoes(quaisCamposTrazer)[sobreUmUnicoRegistro(id), sobreUmConjuntoDeRegistros(ordenacao)], paraSaberSeExistemRegistrosNestasCondicoes(), paraObterAgregacoes(agregacoes,agrupamentos)
	 * facaBuscaNoBancoDeDados(condicoes),envolvendoUmaOuMaisTabelasPara(joins[])[retornarInformacoesSobreUmConjuntoDeDados(ordenacao), paraSaberSeExistemRegistrosNestasCondicoes(), paraObterAgregacoes(agregacoes,agrupamentos)]
	 */
}

// variosregistros: ordenacao, campos, restricoes
// umunicoregistro: restricoes, campos, id
// verificarexistencia: restricoes
// insercao: camposMaisValores
// alteracao: camposMaisValores, id
// exclusao: status, id
// alteracaoBatch : camposMaisValores, restricoes
// exclusaoBatch : status, restricoes
// insercaoBatch: camposMaisValores[] 
// agregacoes : funcoes, restricoes, agrupamentos

// id = 3(umunicoregistro, alteracao, exclusao)
// campos = 2(variosregistros, umunicoregistro)
// restricoes = 5(variosregistros, umunicoregistro,  verificarexistencia, alteracaoBatch, exclusaoBatch)
// camposMaisValores = 3(insercao, alteracao, alteracaoBatch)
// status = 2(exclusaoBatch, exclusao)
// ordenacao = 1
// camposMaisValores[] = 1