package br.com.jobsnow.database.params.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.params.EspecificacaoCampo;
import br.com.jobsnow.database.params.FuncaoAgregacao;
import br.com.jobsnow.database.params.Join;
import br.com.jobsnow.database.params.DatabaseParamsDTO.AusenciaDeCamposDeAgregacaoException;
import br.com.jobsnow.database.params.DatabaseParamsDTO.AusenciaDeCamposMaisValoresException;
import br.com.jobsnow.database.params.DatabaseParamsDTO.CamposDeOrdenacaoNaoDeveriamExistirException;
import br.com.jobsnow.database.params.DatabaseParamsDTO.EntidadeSemTabelaException;
import br.com.jobsnow.database.params.DatabaseParamsDTO.IDDestaEntidadeDeveriaEstarPresenteException;

public class DatabaseParamsDTOTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void retornaFalseSeNaoPossuirCamposParaOrdenacao() {
		boolean possuiOrdenacaoNull = new DatabaseParamsDTO().possuiOrdenacao();
		boolean possuiOrdenacaoVazia = new DatabaseParamsDTO(null, null).possuiOrdenacao();
		
		assertFalse(possuiOrdenacaoNull);
		assertFalse(possuiOrdenacaoVazia);
	}
	
	@Test
	public void retornaTrueSePossuirCamposParaOrdenacao() {
		String[] camposParaSelecionar = null;
		String[] camposParaOrdenacao = {"teste"};
		LinkedHashSet<Join> joins = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = null;
		
		boolean possuiOrdenacao = new DatabaseParamsDTO(camposParaSelecionar, null, joins, restricoes, camposParaOrdenacao).possuiOrdenacao();
		assertTrue(possuiOrdenacao);
	}
	
	@Test
	public void deveLancarCamposDeOrdenacaoNaoDeveriamExistirExceptionAoAssegurarQueCamposDeOrdenacaoNaoEstejamPresentes() {
		String[] camposParaSelecionar = null;
		String[] camposParaOrdenacao = {"teste"};
		LinkedHashSet<Join> joins = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = null;
		DatabaseParamsDTO paramsDTO = new DatabaseParamsDTO(camposParaSelecionar, null, joins, restricoes, camposParaOrdenacao);
		String format = String.format("Os parametros '%s' nao deveriam conter ordenação", paramsDTO);
		
		this.expectedEx.expect(new CamposDeOrdenacaoNaoDeveriamExistirException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		paramsDTO.assegurarQueCamposDeOrdenacaoNaoEstejamPresentes();
	}
	
	@Test
	public void deveLancarIDDestaEntidadeDeveriaEstarPresenteExceptionAoAssegurarQueIdDaEntidadeNaoEstaPresente() {
		DatabaseParamsDTO paramsDTO = new DatabaseParamsDTO();
		String format = String.format("Os parametros '%s' deveriam possuir o id da entidade", paramsDTO);
		
		this.expectedEx.expect(new IDDestaEntidadeDeveriaEstarPresenteException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		paramsDTO.assegurarQueIdDaEntidadeEstaPresente();
	}
	
	@Test
	public void deveLancarEntidadeSemTabelaExceptionAoAssegurarQueEstaEntidadeNaoPossuiTabela() {
		DatabaseParamsDTO paramsDTO = new DatabaseParamsDTO();
		String format = String.format("Os parametros '%s' deveriam possuir uma tabela", paramsDTO);
		
		this.expectedEx.expect(new EntidadeSemTabelaException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		paramsDTO.assegurarQueEstaEntidadePossuiTabela();
		
		paramsDTO = new DatabaseParamsDTO("teste", null);
		format = String.format("Os parametros '%s' deveriam possuir uma tabela", paramsDTO);
		
		this.expectedEx.expect(new EntidadeSemTabelaException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		paramsDTO.assegurarQueEstaEntidadePossuiTabela();
	}
	
	@Test
	public void retornaFalseSeNaoPossuirAgrupamento() {
		LinkedHashSet<FuncaoAgregacao> funcoes = null; 
		LinkedHashSet<Join> joins = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = null;
		String[] camposParaAgrupamento = {};
		DatabaseParamsDTO params = new DatabaseParamsDTO(null,funcoes,null,joins,restricoes,camposParaAgrupamento);
		
		boolean naoPossuiAgrupamentoNull = new DatabaseParamsDTO().possuiAgrupamento();
		boolean naoPossuiAgrupamentoVazio = params.possuiAgrupamento();
		
		assertFalse(naoPossuiAgrupamentoNull);
		assertFalse(naoPossuiAgrupamentoVazio);
	}
	
	@Test
	public void deveLancarAusenciaDeCamposMaisValoresExceptionAoAssegurarQueEstaEntidadeNaoPossuiTantoCamposQuantoValores() {
		DatabaseParamsDTO params = new DatabaseParamsDTO();
		String format = String.format("Os parametros '%s' deveriam possuir campos e valores", params);
		
		this.expectedEx.expect(new AusenciaDeCamposMaisValoresException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		params.assegurarQueEstaEntidadePossuiTantoCamposQuantoValores();
		
		params = new DatabaseParamsDTO(null, new LinkedHashSet<>());
		format = String.format("Os parametros '%s' deveriam possuir campos e valores", params);
		
		this.expectedEx.expect(new AusenciaDeCamposMaisValoresException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		params.assegurarQueEstaEntidadePossuiTantoCamposQuantoValores();
	}
	
	@Test
	public void deveLancarAusenciaDeCamposDeAgregacaoExceptionAoMontarAgregacoesSemFuncoesDeAgregacao() {
		LinkedHashSet<FuncaoAgregacao> funcoes = new LinkedHashSet<>(); 
		LinkedHashSet<Join> joins = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = null;
		String[] camposParaAgrupamento = {};
		
		DatabaseParamsDTO params = new DatabaseParamsDTO();
		String format = String.format("Os parametros '%s' deveriam possuir funcoes de agregacao", params);
		
		this.expectedEx.expect(new AusenciaDeCamposDeAgregacaoException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		params._montaAgregacoes();
		params = new DatabaseParamsDTO(null,funcoes,null,joins,restricoes,camposParaAgrupamento);
		format = String.format("Os parametros '%s' deveriam possuir funcoes de agregacao", params);
		
		this.expectedEx.expect(new AusenciaDeCamposDeAgregacaoException(format).getClass());
		this.expectedEx.expectMessage(format);
		
		params._montaAgregacoes();
	}
	
	@Test
	public void deveMontarAgregacoes() {
		LinkedHashSet<Join> joins = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = null;
		String[] camposParaAgrupamento = {};
		String[] camposParaSelecionar = {"teste"};
		LinkedHashSet<FuncaoAgregacao> funcoes = new LinkedHashSet<>(); 
		funcoes.add(new FuncaoAgregacao("count", "teste"));
		funcoes.add(new FuncaoAgregacao("sum", "teste"));
		
		StringBuilder sqlAgregacoesSemCamposParaSelecionar = new StringBuilder("count(teste),sum(teste)");
		String agregacaoSemCamposParaSelecionar = new DatabaseParamsDTO(null,funcoes,null,joins,restricoes,camposParaAgrupamento)._montaAgregacoes();
		
		StringBuilder sqlAgregacoesComCamposParaSelecionar = new StringBuilder("count(teste),sum(teste), teste");
		String agregacaoComCamposParaSelecionar = new DatabaseParamsDTO(camposParaSelecionar,funcoes,null,joins,restricoes,camposParaAgrupamento)._montaAgregacoes();
		
		assertEquals(sqlAgregacoesSemCamposParaSelecionar.toString(), agregacaoSemCamposParaSelecionar);
		assertEquals(sqlAgregacoesComCamposParaSelecionar.toString(), agregacaoComCamposParaSelecionar);
	}
	
	@Test
	public void deveFazerSelect() {
		StringBuilder sql = new StringBuilder("select outro, xpto from teste \n");
		sql.append("\nleft join tblDir \n");
		sql.append("on tblEsq.campo = tblDir.campo \n");
		sql.append("\n where 1=1 \n");
		sql.append("and tblDir.campo = 1 \n");
		sql.append("order by outro, xpto");
		
		String[] camposParaSelecionar = {"outro, xpto"};
		String[] camposParaOrdenacao = {"outro, xpto"};
		String tabela = "teste";
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		
		joins.add(new Join("tblEsq", "campo", "tblDir", "campo"));
		restricoes.add(new EspecificacaoCampo("tblDir.campo", "1", "Integer"));
		
		String select = new DatabaseParamsDTO(camposParaSelecionar, tabela, joins,restricoes, camposParaOrdenacao)._fazerSelect();
		
		assertEquals(sql.toString(), select);
	}
	
	@Test
	public void naoDeveMontarSqlJoinsSeJoinsEstiverVazioOuNull() {
		StringBuilder sql = new StringBuilder("\n");
		String sqlJoinsNull = new DatabaseParamsDTO()._montaJoins();

		assertEquals(sql.toString(), sqlJoinsNull);
	}
	
	@Test
	public void deveMontarSqlJoins() {
		String[] camposParaOrdenacao = null;
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		joins.add(new Join("tblEsq", "campo", "tblDir", "campo"));
		joins.add(new Join("tblEsq1", "campo1", "tblDir1", "campo1"));
		
		String select = new DatabaseParamsDTO(null, null, joins,null, camposParaOrdenacao)._montaJoins();
		StringBuilder sql = new StringBuilder();
		sql.append("\nleft join tblDir \n");
		sql.append("on tblEsq.campo = tblDir.campo \n");
		sql.append("left join tblDir1 \n");
		sql.append("on tblEsq1.campo1 = tblDir1.campo1 \n");
		
		assertEquals(sql.toString(), select);
	}
	
	@Test
	public void naoDeveMontarSqlWhereSeRestricoesEstiverVazioOuNull() {
		StringBuilder sql = new StringBuilder("\n where 1=1 \n");
		String sqlWhereNull = new DatabaseParamsDTO()._montaWhere();

		assertEquals(sql.toString(), sqlWhereNull);
	}
	
	@Test
	public void deveMontarSqlWhere() {
		String[] camposParaOrdenacao = null;
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		restricoes.add(new EspecificacaoCampo("teste.campo","1","Integer"));
		restricoes.add(new EspecificacaoCampo("coluna.campo","outro","String"));
		
		String where = new DatabaseParamsDTO(null,null,null,restricoes,camposParaOrdenacao)._montaWhere();
		StringBuilder sql = new StringBuilder();
		sql.append("\n where 1=1 \n");
		sql.append("and teste.campo = 1 \n");
		sql.append("and coluna.campo = outro \n");
		
		assertEquals(sql.toString(), where);
	}
	
	@Test
	public void deveFazerSelectPeloId() {
		StringBuilder sql = new StringBuilder("select outro, xpto from teste \n");
		sql.append("\nleft join tblDir \n");
		sql.append("on tblEsq.campo = tblDir.campo \n");
		sql.append("\n where 1=1 \n");
		sql.append("and tblDir.campo = 1 \n");
		sql.append("and teste.id = 5");
		
		String[] camposParaSelecionar = {"outro, xpto"};
		String tabela = "teste";
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		
		joins.add(new Join("tblEsq", "campo", "tblDir", "campo"));
		restricoes.add(new EspecificacaoCampo("tblDir.campo", "1", "Integer"));
		
		String select = new DatabaseParamsDTO(camposParaSelecionar, tabela, joins,restricoes, 5L)._fazerSelectPeloId();
		
		assertEquals(sql.toString(), select);	
	}
	
	@Test
	public void deveFazerSelectDeAgregacao() {
		String[] camposParaAgrupamento = {"teste"};
		String[] camposParaSelecionar = {"teste"};
		String[] camposParaOrdenacao = {"teste"};
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		LinkedHashSet<FuncaoAgregacao> funcoes = new LinkedHashSet<>(); 

		joins.add(new Join("tblEsq", "campo", "tblDir", "campo"));
		restricoes.add(new EspecificacaoCampo("tblDir.campo", "1", "Integer"));
		funcoes.add(new FuncaoAgregacao("count", "teste"));
		funcoes.add(new FuncaoAgregacao("sum", "teste"));
		
		StringBuilder sql = new StringBuilder("select count(teste),sum(teste), teste from tabela\n");
		sql.append("left join tblDir \n");
		sql.append("on tblEsq.campo = tblDir.campo \n");
		sql.append("\n where 1=1 \n");
		sql.append("and tblDir.campo = 1 \n");
		sql.append("group by teste \n");
		sql.append("order by teste");
		
		String select = new DatabaseParamsDTO(camposParaSelecionar, funcoes, "tabela", joins, restricoes,camposParaOrdenacao, camposParaAgrupamento)._fazerSelectDeAgregacao();
		
		assertEquals(sql.toString(), select);
	}
	
	@Test
	public void deveFazerSelectDeVerificacaoDeExistenciaDeRegistro() {
		StringBuilder sql = new StringBuilder("select 1 from ").append("teste").append(" \n");
		sql.append("\nleft join tblDir \n");
		sql.append("on tblEsq.campo = tblDir.campo \n");
		sql.append("\n where 1=1 \n");
		sql.append("and tblDir.campo = 1 \n");
		
		LinkedHashSet<Join> joins = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		
		joins.add(new Join("tblEsq", "campo", "tblDir", "campo"));
		restricoes.add(new EspecificacaoCampo("tblDir.campo", "1", "Integer"));
		
		String select = new DatabaseParamsDTO(null, "teste", joins, restricoes, 5L)._fazerSelectDeVerificacaoDeExistenciaDeRegistro();
		
		assertEquals(sql.toString(), select);
	}
	
	@Test
	public void deveMontarSqlDoUpdate() {
		StringBuilder sql = new StringBuilder("update ").append("teste").append(" set ");
		sql.append("outro = 1,coluna = testando");
		sql.append("\n where 1=1 \n");
		sql.append("and tblDir.campo = 1 \n");
		sql.append(" and teste.id = 5");
		
		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = new LinkedHashSet<>();
		LinkedHashSet<EspecificacaoCampo> restricoes = new LinkedHashSet<>();
		
		restricoes.add(new EspecificacaoCampo("tblDir.campo", "1", "Integer"));
		camposMaisSeusNovosValores.add(new EspecificacaoCampo("outro", "1", "Integer"));
		camposMaisSeusNovosValores.add(new EspecificacaoCampo("coluna", "testando", "String"));
		
		String sqlUpdate = new DatabaseParamsDTO("teste", camposMaisSeusNovosValores, restricoes, 5L)._montarSqlDoUpdate();
		
		assertEquals(sql.toString(), sqlUpdate);
	}
	
	@Test
	public void deveMontarSqlDoInsert() {
		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = new LinkedHashSet<>();
		camposMaisSeusNovosValores.add(new EspecificacaoCampo("outro", "1", "Integer"));
		camposMaisSeusNovosValores.add(new EspecificacaoCampo("novo", "testando", "String"));
		
		StringBuilder sql = new StringBuilder("insert into teste(outro,novo) ");
		sql.append("VALUES (:outro,:novo)");
		
		String sqlDoInsert = new DatabaseParamsDTO("teste", camposMaisSeusNovosValores)._montarSqlDoInsert();
		
		assertEquals(sql.toString(), sqlDoInsert);
	}
	
	@Test
	public void devePegarOsValoresDoInsert() {
		LinkedHashSet<EspecificacaoCampo> camposMaisSeusNovosValores = new LinkedHashSet<>();
		Map<String, Object> valores = new HashMap<>();

		camposMaisSeusNovosValores.add(new EspecificacaoCampo("outro", "1", "Integer"));
		camposMaisSeusNovosValores.add(new EspecificacaoCampo("novo", "testando", "String"));
		valores.put("outro", 1);
		valores.put("novo", "testando");
		
		Map<String, Object> valoresInsert = new DatabaseParamsDTO(null, camposMaisSeusNovosValores)._getValoresInsert();
		
		assertEquals(valores, valoresInsert);
	}
}