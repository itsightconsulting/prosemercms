package com.itsight.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.Logro;
import com.itsight.domain.en.EnLogro;
import com.itsight.service.BeneficiarioService;
import com.itsight.service.LogroService;
import com.itsight.service.TipoEstudioService;
import com.itsight.service.en.EnLogroService;

@Controller
@RequestMapping("/gestion/logro")
public class LogroController {
	
//	private static final Logger LOGGER = LogManager.getLogger(LogroController.class);
			
	private LogroService logroService;
	
	private EnLogroService enLogroService;
	
	private TipoEstudioService tipoEstudioService;
	
	private BeneficiarioService beneficiarioService;				
	
    @Autowired
	public LogroController(
			LogroService logroService,
			TipoEstudioService tipoEstudioService,
			BeneficiarioService beneficiarioService,
			EnLogroService enLogroService) {
		this.logroService = logroService;
		this.tipoEstudioService = tipoEstudioService;
		this.beneficiarioService = beneficiarioService;
		this.enLogroService = enLogroService;
	}
    
    @GetMapping(value = "")
	public ModelAndView principalLogro(Model  model) {
		model.addAttribute("lstBeneficarios", beneficiarioService.listAll());
		return new ModelAndView(ViewConstant.MAIN_LOGRO);
	}
    
	@GetMapping(value = "/nuevo")
	public ModelAndView formularioLogro(Model model) {
		model.addAttribute("type", "logro");
		model.addAttribute("lstBeneficiarios", beneficiarioService.listAll());
		model.addAttribute("lstTipoEstudios",tipoEstudioService.listAll());
		return new ModelAndView(ViewConstant.MAIN_LOGRO_NUEVO);
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String add(
							@ModelAttribute Logro logro, 
							@RequestParam(required = false) String estudioId,
							@RequestParam int beneficiarioId) {
		Logro qLogro;
		EnLogro eLogro;
		String idsAfterRegister = "", firstId; String[] ids;
		if(logro.getId()>0) {
			qLogro = logroService.findOneById(logro.getId());
			qLogro.setFechaLogro(logro.getFechaLogro());
			qLogro.setDescripcion(logro.getDescripcion());
			qLogro.setResumen(logro.getResumen());
			qLogro.setFechaModificacion();
			if(estudioId != null && estudioId.length()>0)
				qLogro.setEstudio(Integer.parseInt(estudioId));
			qLogro.setBeneficiario(beneficiarioId);
			if(qLogro.getMultiple() != null) {//Multiple ids, multiple update
				ids = qLogro.getMultiple().split(",");
				logroService.updateLogroMultiple(ids, qLogro);
			}else {
				logroService.add(qLogro);
			}
		}else {//R E G I S T R O
			String[] estudioIds = estudioId.split(",");
			logro.setFlagActivo(true);
			logro.setFechaCreacion();
			logro.setBeneficiario(beneficiarioId);
			
			//TB INGLES
			eLogro = new EnLogro();
			BeanUtils.copyProperties(logro, eLogro);
			eLogro.setBeneficiario(beneficiarioId);
			
			//Enviando a BD | Por transferencia de estados(Cascade) se registrará tanto el contenido como el logro		
			if(estudioId.equals("")) {
				logroService.add(logro);
				enLogroService.add(eLogro);
			}else {
				for (int i=0; i< estudioIds.length; i++) {
					Logro mLogro = new Logro();
					EnLogro emLogro = new EnLogro();

					//Copiando propiedades
					BeanUtils.copyProperties(logro, mLogro);
					BeanUtils.copyProperties(eLogro, emLogro);
					//Seteando id 
					mLogro.setEstudio(Integer.parseInt(estudioIds[i]));
					emLogro.setEstudio(Integer.parseInt(estudioIds[i]));
					
					if(estudioIds.length>1) {
						if(i>0) {
							mLogro.setFlagCompartido(true);
							emLogro.setFlagCompartido(true);
						}else {
							mLogro.setFlagCompartido(false);
							emLogro.setFlagCompartido(false);
						}
					}
					logroService.add(mLogro);
					enLogroService.add(emLogro);
					idsAfterRegister += mLogro.getId()+",";
				}
				
				//Actualizando columna multiple en caso el logro este asociado a mas de un estudio
				if(estudioIds.length > 1) {
					idsAfterRegister = idsAfterRegister.substring(0, idsAfterRegister.length()-1);
					 firstId = idsAfterRegister.substring(0,idsAfterRegister.indexOf(","));
					 //Sending
					 logro.setId(Integer.parseInt(firstId));
					 logro.setEstudio(Integer.parseInt(estudioIds[0]));
					 logro.setMultiple(idsAfterRegister);
					 eLogro.setId(Integer.parseInt(firstId));
					 eLogro.setEstudio(Integer.parseInt(estudioIds[0]));
					 eLogro.setMultiple(idsAfterRegister);
					 logroService.update(logro);
					 enLogroService.update(eLogro);
				}
			}
			
		}
			return String.valueOf(logro.getId());
	}

	@GetMapping(value = "/obtener")
	public @ResponseBody Logro getLogroById(@RequestParam(value = "id") int logroId) {
		return logroService.findOneWithEstudio(logroId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}/{beneficiario}")
	public @ResponseBody List<Logro> listAll(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("beneficiario") String beneficiarioId) throws JsonProcessingException{
		
			List<Logro> lstLogro;
			
			if(comodin.equals("0") && estado.equals("-1") && beneficiarioId.equals("0") ) {
				lstLogro= logroService.listAll();
			}else {
				if(comodin.equals("0") && beneficiarioId.equals("0")) {
						lstLogro = logroService.findAllByFlagActivo(Boolean.valueOf(estado));
				}else {
					List<Logro> lstLogroFilter = new ArrayList<>();
					comodin = comodin.equals("0") ? "":comodin;
					
					lstLogro = logroService.findAllByResumenContainingIgnoreCase(comodin);
					if(!estado.equals("-1")) {
						for (Logro x : lstLogro) {
								
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
							for (Logro x : lstLogro) {
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
	
	@PutMapping(value = "/desactivar")
	public @ResponseBody String disabled(@RequestParam(value = "id") int id, @RequestParam(required=false) List<Integer> lstIds) {
			Logro logro = logroService.findOneById(id);

			if(logro.isFlagActivo()) {
				logro.setFlagActivo(false);
			}else {
				logro.setFlagActivo(true);
			}
			logro.setFechaModificacion();
			if(lstIds != null) {
				logroService.updateStatusMasive(logro.isFlagActivo(), lstIds);
				enLogroService.updateStatusMasive(logro.isFlagActivo(), lstIds);
			}else {
				logroService.update(logro);
				enLogroService.updateStatusById(logro.isFlagActivo(), id);	
			}
			return "1";
	}
	
	@DeleteMapping(value = "/eliminar/{id}/{isMultiple}")
	public @ResponseBody String eliminar(@PathVariable(value = "id") int id, @PathVariable(value = "isMultiple") String isMultiple){
		boolean flagCompartido = Boolean.valueOf(isMultiple);
		if(flagCompartido){
			logroService.eliminarLogroCompartido(id);
			return "1";
		}else{
			//Cascade
			logroService.delete(id);
			enLogroService.delete(id);
			return "1";
		}
	}
	
}