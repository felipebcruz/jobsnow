package br.com.jobsnow.database.resource;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.jobsnow.database.params.DatabaseParamsDTO;
import br.com.jobsnow.database.service.ServiceDatabase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "API REST para interagir com o banco de dados do jobsnow")
@RestController
@RequestMapping(value = "/api/v1/database/registros", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class DatabaseResource {
	private static final Logger logger = LogManager.getLogger(DatabaseResource.class);

	@Autowired
	private ServiceDatabase srvDatabase;

	@ApiOperation(value = "Retorna os registros selecionados conforme especificados no campo quaisCamposTrazer")
	@GetMapping
	public ResponseEntity<List<Map<String, String>>> _selecioneVariosRegistros(@RequestHeader("params") String parametros) throws Exception {
		DatabaseParamsDTO params = this.convertToDto(parametros);
		logger.info("/api/v1/database/registros - GET > params = " + params);
		List<Map<String, String>> registros = this.srvDatabase._selecioneVariosRegistros(params);

		return ResponseEntity.ok(registros);
	}

	@ApiOperation(value = "Retorna os registros selecionados conforme especificados no campo quaisCamposTrazer baseados no c�digo passado na url")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Map<String, String>> _selecioneUmUnicoRegistro(@PathVariable("id") Long idRegistro, @RequestHeader("params") String parametros) throws Exception {
		DatabaseParamsDTO paramsRequest = this.convertToDto(parametros);
		DatabaseParamsDTO params = new DatabaseParamsDTO(paramsRequest.camposParaSelecionar,paramsRequest.tabela,
				paramsRequest.joins,paramsRequest.restricoes,idRegistro);
		
		Map<String, String> registro = this.srvDatabase._selecioneUmUnicoRegistro(params);

		return ResponseEntity.ok(registro);
	}

	@ApiOperation(value = "Retorna o total conforme o sql descrito no atributo porQuaisCamposAgrupar")
	@GetMapping(value = "/totais")
	public ResponseEntity<Map<String, String>> _obterTotais(@RequestHeader("params") String parametros) throws Exception {
		DatabaseParamsDTO params = this.convertToDto(parametros);
		logger.info("/api/v1/database/registros/totais - GET > params = " + params);
		Map<String, String> total = this.srvDatabase._obterTotais(params);

		return ResponseEntity.ok(total);
	}

	@ApiOperation(value = "Retorna status 200 caso tenha algum registro, e retorna status 204 caso nao tenha nenhum registro conforme sql descrito no atributo")
	@RequestMapping(method = RequestMethod.HEAD)
	public ResponseEntity<Void> _verificarExistenciaDeRegistrosDadasEstasRestricoes(@RequestHeader("params") String parametros) throws Exception {
		DatabaseParamsDTO params = this.convertToDto(parametros);
		logger.info("/api/v1/database/registros - HEAD > params = " + params);

		boolean existeRegistros = srvDatabase._verificarExistenciaDeRegistrosDadasEstasRestricoes(params);

		if (false == existeRegistros) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "Atualiza os registros no banco de dados conforme sql especificados no atributo quaisCamposTrazer")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<Void> _atualizarUmUnicoRegistro(@PathVariable("id") Long idRegistro, @RequestBody DatabaseParamsDTO paramsRequest) throws Exception {
		logger.info("/api/v1/database/registros/{id} - PATCH > params = " + paramsRequest);
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(paramsRequest.tabela, paramsRequest.camposMaisSeusNovosValores, null, idRegistro);
		
		this.srvDatabase._atualizarUmUnicoRegistro(params);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Deleta o registro com o codigo da url e com status conforme especificados no atributo valorDoStatus")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> _alterarStatusDeUmUnicoRegistro(@PathVariable("id") Long idRegistro,	@RequestBody DatabaseParamsDTO paramsRequest) throws Exception {
		logger.info("/api/v1/database/registros/{id} - DELETE > params = " + paramsRequest);
		
		DatabaseParamsDTO params = new DatabaseParamsDTO(paramsRequest.tabela, paramsRequest.camposMaisSeusNovosValores, null, idRegistro);
		
		this.srvDatabase._atualizarUmUnicoRegistro(params);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Insere no banco o registro especificado")
	@PostMapping()
	public ResponseEntity<Long> _inserirUmUnicoRegistro(@RequestBody DatabaseParamsDTO params) throws Exception {
		logger.info("/api/v1/database/registros - POST > params = " + params);
		Long idRegistro = this.srvDatabase._inserirUmUnicoRegistro(params);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idRegistro).toUri();

		return ResponseEntity.created(location).build();
	}

	@ApiOperation(value = "Retorna os nomes dos campos existentes da tabela")
	@GetMapping(value = "/campos/{tabela}")
	public ResponseEntity<Set<String>> _getCamposTabela(@PathVariable("tabela") String tabela) throws Exception{
		logger.info("/api/v1/database/registros/campos/tabela - GET > tabela = " + tabela);
		String tblComNomesDosCampos = "information_schema.columns"; 
		Set<String> campos = this.srvDatabase._getCamposTabela(tabela, tblComNomesDosCampos);
		
		return ResponseEntity.ok(campos);
	}
	

	private DatabaseParamsDTO convertToDto(String headerParams)
			throws IOException, JsonParseException, JsonMappingException {
		ObjectMapper objectMapper = new ObjectMapper();
		DatabaseParamsDTO params = objectMapper.readValue(headerParams, DatabaseParamsDTO.class);
		return params;
	}
}