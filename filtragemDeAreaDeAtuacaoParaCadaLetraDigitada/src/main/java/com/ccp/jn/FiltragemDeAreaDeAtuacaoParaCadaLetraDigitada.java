package com.ccp.jn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.bancoDeDados.restInterface.Tabela;

@RestController

public class FiltragemDeAreaDeAtuacaoParaCadaLetraDigitada {

	@ResponseBody
	@GetMapping("/areasDeAtuacao")
	public List<Map<String, String>> executar(@RequestParam("caracteresDigitados") String caracteresDigitados) {

		Tabela areasDeAtuacao = new Tabela("areasDeAtuacao");

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

}
