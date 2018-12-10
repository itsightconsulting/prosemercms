package com.itsight.controller.en;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.domain.en.EnCapacitacion;
import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.service.en.EnCapacitacionService;
import com.itsight.service.en.EnContenidoWebService;

@Controller
@RequestMapping("/en/gestion/capacitacion")
public class EnCapacitacionController {
	
	
	private EnCapacitacionService enCapacitacionService;
	
	private EnContenidoWebService contenidoWebService;	
		
    @Autowired
	public EnCapacitacionController(
			EnCapacitacionService enCapacitacionService,
			EnContenidoWebService contenidoWebService) {
    	this.enCapacitacionService = enCapacitacionService;
		this.contenidoWebService = contenidoWebService;
	}
    
	@GetMapping(value = "/obtener")
	public @ResponseBody EnCapacitacion getCapacitacionById(@RequestParam(value = "id") int capacitacionId) {
		return enCapacitacionService.findOneById(capacitacionId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}/{beneficiario}")
	public @ResponseBody List<EnCapacitacion> listAllTrainings(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("beneficiario") String beneficiarioId) throws JsonProcessingException{
		
		List<EnCapacitacion> lstCapacitacion = new ArrayList<EnCapacitacion>();
		
		if(comodin.equals("0") && estado.equals("-1") && beneficiarioId.equals("0") ) {
			lstCapacitacion= enCapacitacionService.listAll();
		}else {
			if(comodin.equals("0") && beneficiarioId.equals("0")) {
					lstCapacitacion = enCapacitacionService.findAllByFlagActivo(Boolean.valueOf(estado));
			}else {
				List<EnCapacitacion> lstCapacitacionFilter = new ArrayList<>();
				comodin = comodin.equals("0") ? "":comodin;
				
				lstCapacitacion = enCapacitacionService.findAllByTituloPrincipalContainingIgnoreCase(comodin);
				if(!estado.equals("-1")) {
					for (EnCapacitacion x : lstCapacitacion) {
							
						if(!beneficiarioId.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())) {
							if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
								lstCapacitacionFilter.add(x);
							}
						}else if(beneficiarioId.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())){
								lstCapacitacionFilter.add(x);
						}
					}
					
					return lstCapacitacionFilter;
				}else {
					if(!beneficiarioId.equals("0")) {
						for (EnCapacitacion x : lstCapacitacion) {
							if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
								lstCapacitacionFilter.add(x);
							}
						}
						return lstCapacitacionFilter;
					}
						
				}
			}
		}
		return lstCapacitacion;
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addTraining(
			@ModelAttribute EnCapacitacion capacitacion,
			@RequestParam boolean flagResumen,
			@RequestParam(value="resumen", required = false) String txtResumen) {
		
			if(capacitacion.getId()> 0) {//Actualizacion
				EnContenidoWeb contenidoWeb = null;
				EnCapacitacion qCapacitacion = enCapacitacionService.findOneById(capacitacion.getId());
				qCapacitacion.setTituloPrincipal(capacitacion.getTituloPrincipal());
				qCapacitacion.setTituloLargo(capacitacion.getTituloLargo());
				qCapacitacion.setDescripcion(capacitacion.getDescripcion());
				qCapacitacion.setFechaCapacitacion(capacitacion.getFechaCapacitacion());
				qCapacitacion.setTags(capacitacion.getTags());
				contenidoWeb = contenidoWebService.findOneById(qCapacitacion.getContenidoWeb().getId());
				contenidoWeb.setTitulo(qCapacitacion.getTituloPrincipal());
				contenidoWeb.setUrl(2, qCapacitacion.getTituloPrincipal().toLowerCase().replace(" ", "-"));
				contenidoWeb.setTags(capacitacion.getTags());
				contenidoWeb.addEnCapacitacion(qCapacitacion);
				//Resumen
				contenidoWeb.getResumen().setResumen(txtResumen);
				contenidoWeb.getResumen().setTitulo(contenidoWeb.getTitulo());
				contenidoWeb.getResumen().setUrl(contenidoWeb.getUrl());
				
				contenidoWebService.add(contenidoWeb);
				return String.valueOf(contenidoWeb.getId());
			}
			return "-1";
	}
	
}