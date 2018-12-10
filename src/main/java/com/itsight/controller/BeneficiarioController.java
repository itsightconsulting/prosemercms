package com.itsight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.domain.Beneficiario;
import com.itsight.service.BeneficiarioService;

@Controller
@RequestMapping(value = "/gestion/beneficiario")
public class BeneficiarioController {
	
	@Autowired
	private BeneficiarioService beneficiarioService;
	
	@GetMapping(value = "/listarTodos")
	public @ResponseBody List<Beneficiario> listAllBeneficiary() throws JsonProcessingException {
			return beneficiarioService.listAll();	
	}

}
