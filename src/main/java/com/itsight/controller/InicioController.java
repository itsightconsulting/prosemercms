package com.itsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.itsight.constants.ViewConstant;
import com.itsight.service.BeneficiarioService;
import com.itsight.service.TipoEstudioService;

@Controller
@RequestMapping(value = "/gestion/inicio")
public class InicioController {
	
	private TipoEstudioService tipoEstudioService;
	
	private BeneficiarioService beneficiarioService;

	@Autowired
	public InicioController(TipoEstudioService tipoEstudioService, BeneficiarioService beneficiarioService) {
		// TODO Auto-generated constructor stub
		this.tipoEstudioService = tipoEstudioService;
		this.beneficiarioService = beneficiarioService;
	}
	
	@GetMapping(value = "/estudio")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView principalEstudio(Model  model) {
		model.addAttribute("lstTipoEstudios", tipoEstudioService.listAll());
		model.addAttribute("lstBeneficarios", beneficiarioService.listAll());
		return new ModelAndView(ViewConstant.MAIN_INICIO_ESTUDIO);
	}
	
	@GetMapping(value = "/capacitacion")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView principalCapacitacion(Model  model) {
		return new ModelAndView(ViewConstant.MAIN_INICIO_CAPACITACION);
	}

}
