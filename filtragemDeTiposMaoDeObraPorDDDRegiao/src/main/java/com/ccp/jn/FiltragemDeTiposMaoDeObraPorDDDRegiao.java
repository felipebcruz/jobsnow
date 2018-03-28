package com.ccp.jn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.bancoDeDados.restInterface.Tabela;

@RestController
@CrossOrigin 
public class FiltragemDeTiposMaoDeObraPorDDDRegiao {
	
	@ResponseBody
	@GetMapping("/tiposDeMaoDeObra")
	public List<Map<String, String>> executar(@RequestParam("ddd") Integer ddd) {
		
		Tabela tiposDeMaoDeObra = new Tabela("tiposDeMaoDeObra");
		
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
}
