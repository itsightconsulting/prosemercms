package com.itsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.domain.Perfil;
import com.itsight.service.PerfilService;

@Controller
@RequestMapping(value = "/gestion/perfil")
public class PerfilController {
	
	@Autowired
	private PerfilService perfilService;
	
	@GetMapping(value = "/listarTodos")
	public @ResponseBody List<Perfil> listAllVideoTypeWithoutFilters() throws JsonProcessingException {
			return perfilService.listAll();	
	}

}
