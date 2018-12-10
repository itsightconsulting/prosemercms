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
import com.itsight.domain.en.EnEstudio;
import com.itsight.service.en.EnContenidoWebService;
import com.itsight.service.en.EnEstudioService;

@Controller
@RequestMapping("/en/gestion/estudio")
public class EnEstudioController {
			
	private EnEstudioService enEstudioService;
	
	private EnContenidoWebService contenidoWebService;	
			
	//E N G L I S H  S E R V I C E S
		
    @Autowired
	public EnEstudioController(
			EnEstudioService enEstudioService, 
			EnContenidoWebService contenidoWebService) {
		this.enEstudioService = enEstudioService;
		this.contenidoWebService = contenidoWebService;
	}
    
    @PostMapping(value = "/agregar")
	public @ResponseBody String addResearch(
				@ModelAttribute EnEstudio estudio, 
				@RequestParam boolean flagResumen,
				@RequestParam(value="resumen", required = false) String txtResumen) {

			if(estudio.getId()> 0) {
				EnContenidoWeb contenidoWeb;
				EnEstudio qEstudio;
				qEstudio = enEstudioService.findOneById(estudio.getId());
				qEstudio.setTituloPrincipal(estudio.getTituloPrincipal());
				qEstudio.setTituloLargo(estudio.getTituloLargo());
				qEstudio.setAlcance(estudio.getAlcance());
				qEstudio.setTags(estudio.getTags());
				qEstudio.setFechaEstudio(estudio.getFechaEstudio());
				contenidoWeb = contenidoWebService.findOneById(qEstudio.getContenidoWeb().getId());
				contenidoWeb.setTitulo(qEstudio.getTituloPrincipal());
				contenidoWeb.setUrl(1, qEstudio.getTituloPrincipal().toLowerCase().replace(" ", "-"));
				contenidoWeb.setTags(estudio.getTags());
				contenidoWeb.addEnEstudio(qEstudio);
				//Resumen
				contenidoWeb.getResumen().setResumen(txtResumen);
				contenidoWeb.getResumen().setTitulo(contenidoWeb.getTitulo());
				contenidoWeb.getResumen().setUrl(contenidoWeb.getUrl());
				
				//Enviando a BD | Por transferencia de estados(Cascade) se registrará tanto el contenido como el estudio
				contenidoWebService.add(contenidoWeb);
				return String.valueOf(contenidoWeb.getId());
			}
			return "-1";
			
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody EnEstudio getEstudioById(@RequestParam(value = "id") int estudioId) {
		return enEstudioService.findOneWithTipoEstudioById(estudioId);
	}
    
    @GetMapping(value = "/obtenerListado/{comodin}/{estado}/{beneficiario}")
	public @ResponseBody List<EnEstudio> listAllResearchs(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("beneficiario") String beneficiarioId){
		
		List<EnEstudio> lstEstudio;
		
		if(comodin.equals("0") && estado.equals("-1") && beneficiarioId.equals("0") ) {
			lstEstudio= enEstudioService.listAll();
		}else {
			if(comodin.equals("0") && beneficiarioId.equals("0")) {
					lstEstudio = enEstudioService.findAllByFlagActivo(Boolean.valueOf(estado));
			}else {
				List<EnEstudio> lstEstudioFilter = new ArrayList<>();
				comodin = comodin.equals("0") ? "":comodin;
				
				lstEstudio = enEstudioService.findAllByTituloPrincipalContainingIgnoreCase(comodin);
				if(!estado.equals("-1")) {
					for (EnEstudio x : lstEstudio) {
							
						if(!beneficiarioId.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())) {
							if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
								lstEstudioFilter.add(x);
							}
						}else if(beneficiarioId.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())){
								lstEstudioFilter.add(x);
						}
					}
					
					return lstEstudioFilter;
				}else {
					if(!beneficiarioId.equals("0")) {
						for (EnEstudio x : lstEstudio) {
							if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
								lstEstudioFilter.add(x);
							}
						}
						return lstEstudioFilter;
					}
						
				}
			}
		}
		return lstEstudio;
	}
    
    @GetMapping(value = "/obtenerListado/top/{tipo}/{beneficiario}/{comodin}")
	public @ResponseBody List<EnEstudio> list(
										@PathVariable("tipo") String tipo,
										@PathVariable("beneficiario") String beneficiario,
										@PathVariable("comodin") String comodin) throws JsonProcessingException{
		
		List<EnEstudio> lstEstudio = new ArrayList<EnEstudio>();
		if(tipo.equals("0")) {
			lstEstudio = enEstudioService.listTopByTituloPrincipalAndBeneficiario(comodin, Integer.parseInt(beneficiario));
		}else {
			lstEstudio = enEstudioService.listTopByTipoEstudioIdAndBeneficiario(Integer.parseInt(tipo), Integer.parseInt(beneficiario));
		}
		
		return lstEstudio;
	}
	
}