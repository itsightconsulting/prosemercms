package com.itsight.controller.en;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.domain.en.EnQuienesSomos;
import com.itsight.service.en.EnQuienesSomosService;

@Controller
@RequestMapping("/en/gestion/quienes-somos")
public class EnQuienesSomosController {
				
	private EnQuienesSomosService quienesSomosService;
		
    @Autowired
	public EnQuienesSomosController(EnQuienesSomosService quienesSomosService) {
		this.quienesSomosService = quienesSomosService;
	}
	
	@PostMapping(value = "/guardar")
	public @ResponseBody String addWhoAreUs(
							@ModelAttribute EnQuienesSomos quienesSomos) {
		
		if(quienesSomos.getId()>0 && quienesSomos.getId()<5) {
			EnQuienesSomos qQuienesSomos = quienesSomosService.findOneById(quienesSomos.getId());
			qQuienesSomos.setContenido(quienesSomos.getContenido());
			qQuienesSomos.setFechaModificacion();
			qQuienesSomos.setModificadoPor(SecurityContextHolder.getContext().getAuthentication().getName());
			quienesSomosService.add(qQuienesSomos);
			return String.valueOf(quienesSomos.getId());
		}else {
			return "-99";
		}
	}

	@GetMapping(value = "/obtener")
	public @ResponseBody EnQuienesSomos getQuienesSomosById(@RequestParam(value = "id") int quienesSomosId) {
		return quienesSomosService.findOneById(quienesSomosId);
	}
	
	@GetMapping(value = "/obtenerListado")
	public @ResponseBody List<EnQuienesSomos> listAllWhoAreUs() throws JsonProcessingException{
		return quienesSomosService.listAll();
	}
	
}