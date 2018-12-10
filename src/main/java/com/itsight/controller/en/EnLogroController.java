package com.itsight.controller.en;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import com.itsight.domain.en.EnLogro;
import com.itsight.service.en.EnLogroService;

@Controller
@RequestMapping("/en/gestion/logro")
public class EnLogroController {
	
//	private static final Logger LOGGER = LogManager.getLogger(LogroController.class);
			
	private EnLogroService enLogroService;
				
    @Autowired
	public EnLogroController(
			EnLogroService enLogroService,
			HttpSession session) {
		this.enLogroService = enLogroService;
	}
    
	@PostMapping(value = "/agregar")
	public @ResponseBody String add(
							@ModelAttribute EnLogro logro, 
							@RequestParam(required = false) Integer estudioId,
							@RequestParam int beneficiarioId) {
		String[] ids = null;
		if(logro.getId()>0) {
			EnLogro qLogro = enLogroService.findOneById(logro.getId());
			qLogro.setFechaLogro(logro.getFechaLogro());
			qLogro.setDescripcion(logro.getDescripcion());
			qLogro.setResumen(logro.getResumen());
			qLogro.setFechaModificacion();
			if(estudioId != null && estudioId >0)
				logro.setEstudio(estudioId);
			logro.setBeneficiario(beneficiarioId);
			if(qLogro.getMultiple() != null) {//Multiple ids, multiple update
				ids = qLogro.getMultiple().split(",");
				enLogroService.updateLogroMultiple(ids, qLogro);
			}else {
				enLogroService.add(qLogro);
			}
		}
		
		return String.valueOf(logro.getId());
	}

	@GetMapping(value = "/obtener")
	public @ResponseBody EnLogro getLogroById(@RequestParam(value = "id") int logroId) {
		return enLogroService.findOneWithEstudio(logroId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}/{beneficiario}")
	public @ResponseBody List<EnLogro> listAllAchievements(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("beneficiario") String beneficiarioId) throws JsonProcessingException{
		
			List<EnLogro> lstLogro = new ArrayList<EnLogro>();
			
			if(comodin.equals("0") && estado.equals("-1") && beneficiarioId.equals("0") ) {
				lstLogro= enLogroService.listAll();
			}else {
				if(comodin.equals("0") && beneficiarioId.equals("0")) {
						lstLogro = enLogroService.findAllByFlagActivo(Boolean.valueOf(estado));
				}else {
					List<EnLogro> lstLogroFilter = new ArrayList<>();
					comodin = comodin.equals("0") ? "":comodin;
					
					lstLogro = enLogroService.findAllByResumenContainingIgnoreCase(comodin);
					if(!estado.equals("-1")) {
						for (EnLogro x : lstLogro) {
								
							if(!beneficiarioId.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())) {
								if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
									lstLogroFilter.add(x);
								}
							}else if(beneficiarioId.equals("0") && Boolean.valueOf(estado).equals(x.isFlagActivo())){
									lstLogroFilter.add(x);
							}
						}
						
						return lstLogroFilter;
					}else {
						if(!beneficiarioId.equals("0")) {
							for (EnLogro x : lstLogro) {
								if(beneficiarioId.equals(String.valueOf(x.getBeneficiario().getId()))){
									lstLogroFilter.add(x);
								}
							}
							return lstLogroFilter;
						}
							
					}
				}
			}
			return lstLogro;
	}
	
}