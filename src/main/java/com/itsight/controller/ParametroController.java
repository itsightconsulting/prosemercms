package com.itsight.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.Parametro;
import com.itsight.service.ParametroService;

@Controller
@RequestMapping("/gestion/parametro")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ParametroController {
	
//	private static final Logger LOGGER = LogManager.getLogger(ParametroController.class);
	
	private ParametroService parametroService;
	
	@Autowired
	public ParametroController(ParametroService parametroService) {
		// TODO Auto-generated constructor stub
		this.parametroService = parametroService;
	}
	
	@GetMapping(value = "")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView principalParametro(Model model) {
		model.addAttribute("type", "parametro");
		return new ModelAndView(ViewConstant.MAIN_PARAMETRO);
	}
	
	@GetMapping(value = "/listarTodos")
	public @ResponseBody List<Parametro> listAllCategoriesWithoutFilters() throws JsonProcessingException {
			return parametroService.listAll();	
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}")
	public @ResponseBody List<Parametro> listAllParameter(
										@PathVariable("comodin") String comodin) throws JsonProcessingException{
		List<Parametro> lstParametro = new ArrayList<Parametro>();
		if(comodin.equals("0")) {
			lstParametro = parametroService.listAll();	
		}else {
			lstParametro = parametroService.findAllByClaveContainingIgnoreCase(comodin);	
		}
		return lstParametro;
	}
	
	@GetMapping(value = "/obtenerParametro")
	public @ResponseBody Parametro getParameterById(@RequestParam(value = "id") int parametroId) {
		return parametroService.getParametroById(parametroId);
	}
	
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/agregar")
	public @ResponseBody String add(@ModelAttribute Parametro parametro) {
			
		
			if(parametro.getId() == 0 ){
				parametroService.add(parametro);
				return String.valueOf(parametro.getId());
			}else {
				Parametro qParametro = parametroService.getParametroById(parametro.getId());
				qParametro.setClave(parametro.getClave());
				qParametro.setValor(parametro.getValor());
				parametroService.update(parametro);
				return String.valueOf(parametro.getId());
			}
	}
	
}
