package com.itsight.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.Parseador;
import com.itsight.domain.ContenidoWeb;
import com.itsight.domain.Estudio;
import com.itsight.domain.Parametro;
import com.itsight.service.ContenidoWebService;
import com.itsight.service.EstudioService;
import com.itsight.service.ParametroService;

@Controller
@RequestMapping("/gestion/contenido-web")
public class ContenidoWebController {
	
	private ContenidoWebService contenidoWebService;
	
	private EstudioService estudioService;
	
	private ParametroService parametroService;
	
	private ServletContext context;

	@Autowired
	public ContenidoWebController(ContenidoWebService contenidoWebService, EstudioService estudioService, ParametroService parametroService, ServletContext context) {
		// TODO Auto-generated constructor stub
		this.contenidoWebService = contenidoWebService;
		this.estudioService = estudioService;
		this.parametroService = parametroService;
		this.context = context;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/obtenerListado/estudio/{comodin}/{tipoEstudio}/{beneficiario}")
	public @ResponseBody List<ContenidoWeb> listContenidoWebByStudyTypePrincipales(
										@PathVariable("comodin") String comodin,
										@PathVariable("tipoEstudio") String tipoEstudioId,
										@PathVariable("beneficiario") String beneficiarioId) throws JsonProcessingException{
		List<Integer> lstEstudioIds;
		if(comodin.equals("0") &&  tipoEstudioId.equals("0") && beneficiarioId.equals("0")) {
			lstEstudioIds = estudioService.findAllIdsByFlagActivo(true);
		}else if(comodin.equals("0") && !tipoEstudioId.equals("0") && beneficiarioId.equals("0")) {
			lstEstudioIds = estudioService.findAllIdsByFlagActivoAndTipoEstudioId(true, Integer.parseInt(tipoEstudioId));
		}else if(comodin.equals("0") && tipoEstudioId.equals("0") && !beneficiarioId.equals("0")){
			lstEstudioIds = estudioService.findAllIdsByFlagActivoAndBeneficiarioId(true, Integer.parseInt(beneficiarioId));
		}else {
			lstEstudioIds = new ArrayList<>();
			comodin = comodin.equals("0") ? "":comodin;
			List<Estudio> lstEstudio = estudioService.findAllIdsByFlagActivoAndTituloPrincipalContainingIgnoreCase(true, comodin);
			
			if(!tipoEstudioId.equals("0")) {
				for (Estudio x : lstEstudio) {
					if(!beneficiarioId.equals("0") && tipoEstudioId.equals(String.valueOf(x.getTipoEstudio().getId()))) {
						if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
							lstEstudioIds.add(x.getId());
						}
					} else if(beneficiarioId.equals("0") && tipoEstudioId.equals(String.valueOf(x.getTipoEstudio().getId()))) {
							lstEstudioIds.add(x.getId());
					}
				}
			}else {
				if(!beneficiarioId.equals("0")) {
					for (Estudio x : lstEstudio) {
						if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
							lstEstudioIds.add(x.getId());
						}
					}
				}else {
					for (Estudio x : lstEstudio) {
						lstEstudioIds.add(x.getId());
					}
				}
			}
		}
		
		if(lstEstudioIds.isEmpty()){
			return new ArrayList<>();
		}
		return contenidoWebService.findAllForInicioByIdIn(lstEstudioIds);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/obtenerListado/{tipoContenido}/{comodin}")
	public @ResponseBody List<ContenidoWeb> listContenidoWebByType(
										@PathVariable("tipoContenido") int tipoContenido,
										@PathVariable("comodin") String comodin) throws JsonProcessingException{
		List<ContenidoWeb> lstContenidoWeb = null;
		if(!comodin.equals("0")) {
			lstContenidoWeb = contenidoWebService.findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(tipoContenido, comodin);
		}else {
			lstContenidoWeb = contenidoWebService.findAllForInicioByTipoContenidoId(tipoContenido);
		}
		if(lstContenidoWeb.isEmpty()){
			return new ArrayList<ContenidoWeb>();
		}
		return lstContenidoWeb;
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping(value = "/desactivar/inicio")
	public @ResponseBody String disabledContentForInitPage(
								@RequestParam(value = "id") int id,
								@RequestParam(value = "tipoContenido") int tipoContenido, 
								@RequestParam(value = "flagInicio") boolean flagInicio,
								@RequestParam(value = "tipoEstudioId", required = false) Integer tipoEstudioId) {
			String parameterName = "";
			switch (tipoContenido) {
			case 1:
				tipoEstudioId = estudioService.findTipoEstudioIdByContenidoWebId(id);
				if(tipoEstudioId == 1) {
					parameterName = "INDEX_PAGE_STUDIES_1";
				}else {
					parameterName = "INDEX_PAGE_STUDIES_2";
				}	
				break;

			default:
				parameterName = "INDEX_PAGE_TRAININGS";
				break;
			}
			Parametro qParametro = parametroService.findByClave(parameterName);
			String genericoIdsInicio =  qParametro.getValor();
			//Actualizando el flagInicio de la tabla contenido web
			try {
				contenidoWebService.updatingFlagInicioById(id, flagInicio);
				genericoIdsInicio = genericoIdsInicio.equals("0")?"":genericoIdsInicio; 
				if(flagInicio) {
					if(genericoIdsInicio.equals("")) {
						genericoIdsInicio = String.valueOf(id);
					}else if(genericoIdsInicio.equals(String.valueOf(id))){
						//genericoIdsInicio = String.valueOf(id);	
					}else {
						genericoIdsInicio += ","+id;	
					}
					
				}else {
					if(genericoIdsInicio.equals(String.valueOf(id))){
						genericoIdsInicio = "";
					}else {
						if(genericoIdsInicio.startsWith(id+",")) {
							genericoIdsInicio = genericoIdsInicio.replace(id+",", "");
						}else if(genericoIdsInicio.endsWith(","+id)) {
							genericoIdsInicio = genericoIdsInicio.replace(","+id, "");
						}else {
							genericoIdsInicio = genericoIdsInicio.replace(","+id+",", ",");
						}
					}
				}
				
				String[] arrayString = genericoIdsInicio.split(",");
				//Ordenando los ids
				Integer[] arrayInt = Parseador.stringArrayToIntArray(arrayString);
				Arrays.sort(arrayInt);
				//Agregando al servlet context y convirtiendo de string a int array
				context.setAttribute(parameterName, arrayInt);
				//Actualizando el parametro
				qParametro.setValor(StringUtils.join(arrayInt, ","));
				qParametro.setFechaModificacion();
				qParametro.setModificadoPor(SecurityContextHolder.getContext().getAuthentication().getName());
				parametroService.update(qParametro);
			
			} catch (Exception e) {
				// TODO: handle exception
				return "-1";
			}
			return "1";
	}
}
