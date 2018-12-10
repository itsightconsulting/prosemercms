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
import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.domain.en.EnEvento;
import com.itsight.service.en.EnContenidoWebService;
import com.itsight.service.en.EnEventoService;

@Controller
@RequestMapping("/en/gestion/evento")
public class EnEventoController {
		
	private EnEventoService eventoService;
			
	private EnContenidoWebService contenidoWebService;
	
    @Autowired
	public EnEventoController(
			EnEventoService eventoService, 
			EnContenidoWebService contenidoWebService) {
		this.eventoService = eventoService;
		this.contenidoWebService = contenidoWebService;
	}
    
	@PostMapping(value = "/agregar")
	public @ResponseBody String addEvent(
				@ModelAttribute EnEvento evento, 
				@RequestParam boolean flagResumen,
				@RequestParam(value="resumen", required = false) String txtResumen) {
			
		if(evento.getId()> 0) {
			EnContenidoWeb contenidoWeb = null;

			EnEvento qEvento = eventoService.findOneById(evento.getId());
			qEvento.setTituloPrincipal(evento.getTituloPrincipal());
			qEvento.setTituloLargo(evento.getTituloLargo());
			qEvento.setDescripcion(evento.getDescripcion());
			qEvento.setFechaEvento(evento.getFechaEvento());
			qEvento.setTags(evento.getTags());
			contenidoWeb = contenidoWebService.findOneById(qEvento.getContenidoWeb().getId());
			contenidoWeb.setTitulo(qEvento.getTituloPrincipal());
			contenidoWeb.setUrl(3, qEvento.getTituloPrincipal().toLowerCase().replace(" ", "-"));
			contenidoWeb.setTags(evento.getTags());
			contenidoWeb.addEnEvento(qEvento);
			//Resumen
			contenidoWeb.getResumen().setResumen(txtResumen);
			contenidoWeb.getResumen().setTitulo(contenidoWeb.getTitulo());
			contenidoWeb.getResumen().setUrl(contenidoWeb.getUrl());

			contenidoWebService.add(contenidoWeb);
			return String.valueOf(contenidoWeb.getId());
		}
		
		return "-1";
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody EnEvento getEventoById(@RequestParam(value = "id") int eventoId) {
		return eventoService.findOneWithContenidoWebById(eventoId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}")
	public @ResponseBody List<EnEvento> listAllEvents(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado) throws JsonProcessingException{

    	List<EnEvento> lstEvento;
			if(comodin.equals("0") && estado.equals("-1")) {
				lstEvento = eventoService.listAll();
			}else {
				if(comodin.equals("0") && !estado.equals("-1")) {
					lstEvento = eventoService.findAllByFlagActivo(Boolean.valueOf(estado));
				}else {
					comodin = comodin.equals("0") ? "":comodin;
					lstEvento = eventoService.findAllByTituloPrincipalContainingIgnoreCase(comodin);

					if(!estado.equals("-1")) {
						List<EnEvento> lstEventoFilter = new ArrayList<>();

						for (EnEvento x : lstEvento) {
							if(Boolean.valueOf(estado).equals(x.isFlagActivo())) {
									lstEventoFilter.add(x);
							}
						}
						return lstEventoFilter;
					}
				}
				
			}
		return lstEvento;
    }
	
}