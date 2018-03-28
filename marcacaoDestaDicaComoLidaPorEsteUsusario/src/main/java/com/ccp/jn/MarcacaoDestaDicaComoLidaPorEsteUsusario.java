package com.ccp.jn;

import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.bancoDeDados.restInterface.Tabela;

@CrossOrigin
@RestController
public class MarcacaoDestaDicaComoLidaPorEsteUsusario {
	
	@ResponseBody
	@PostMapping("usuarios/{idDoUsuario}/dicasProfissionais/NaoLidas/{idDaDica}")
	public void executar(@PathVariable("idDoUsuario") Long idDoUsuario, @PathVariable("idDaDica") Long idDaDica) {
		
		//insert into dica_usuario (remetente, idUsuario, data, idDica, status) values (0, :idUsuario, getDate(), :idDica, 1)
		HashMap<String, String> marcacaoDestaDicaComoLida = new HashMap<>();
		
		marcacaoDestaDicaComoLida.put("remetente", "0");
		marcacaoDestaDicaComoLida.put("idUsuario", "" + idDoUsuario);
		marcacaoDestaDicaComoLida.put("data", "" + System.currentTimeMillis());
		marcacaoDestaDicaComoLida.put("idDica", "" + idDaDica);
		marcacaoDestaDicaComoLida.put("status", "1");
		
		Tabela dicaUsuario = new Tabela("dica_usuario");
		
		dicaUsuario._inserirUmUnicoRegistro(marcacaoDestaDicaComoLida);
		
		
		
	}
}
