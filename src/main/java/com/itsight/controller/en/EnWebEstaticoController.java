package com.itsight.controller.en;

import java.text.ParseException;
import java.util.*;

import javax.servlet.ServletContext;

import com.itsight.domain.Estudio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itsight.constants.Parseador;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.en.EnCapacitacion;
import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.domain.en.EnEstudio;
import com.itsight.domain.en.EnEvento;
import com.itsight.domain.en.EnLogro;
import com.itsight.service.ContenidoArchivoService;
import com.itsight.service.ContenidoImagenService;
import com.itsight.service.EstudioService;
import com.itsight.service.SliderService;
import com.itsight.service.TagService;
import com.itsight.service.en.EnCapacitacionService;
import com.itsight.service.en.EnContenidoWebService;
import com.itsight.service.en.EnEstudioService;
import com.itsight.service.en.EnEventoService;
import com.itsight.service.en.EnLogroService;
import com.itsight.service.en.EnMemoriaService;
import com.itsight.service.en.EnQuienesSomosService;
import com.itsight.service.en.EnResumenService;

@Controller
@RequestMapping("/web/en")

public class EnWebEstaticoController {

	public static final Logger LOGGER = LogManager.getLogger(EnWebEstaticoController.class);
		
	private EnContenidoWebService contenidoWebService;
	
	private SliderService sliderService;
	
	private EnResumenService resumenService;
	
	private ContenidoArchivoService contenidoArchivoService;
	
	private ContenidoImagenService contenidoImagenService;
	
	private EstudioService esEstudioService;
	
	private EnEstudioService estudioService;
	
	private EnCapacitacionService capacitacionService;
	
	private EnEventoService eventoService;
	
	private EnLogroService logroService;
	
	private EnQuienesSomosService quienesSomosService;
	
	private EnMemoriaService memoriaService;
	
	private TagService tagService;
	
	private ServletContext context;
	
	@Autowired
	public EnWebEstaticoController(
						EnContenidoWebService contenidoWebService,
						SliderService sliderService,
						EnResumenService resumenService,
						ContenidoArchivoService contenidoArchivoService,
						ContenidoImagenService contenidoImagenService,
						EstudioService esEstudioService,
						EnEstudioService estudioService,
						EnCapacitacionService capacitacionService,
						EnEventoService eventoService,
						EnLogroService logroService,
						EnQuienesSomosService quienesSomosService,
						TagService tagService,
						EnMemoriaService memoriaService,
						ServletContext context) {
		this.contenidoWebService = contenidoWebService;
		this.sliderService = sliderService;
		this.resumenService = resumenService;
		this.contenidoArchivoService = contenidoArchivoService;
		this.contenidoImagenService = contenidoImagenService;
		this.esEstudioService = esEstudioService;
		this.estudioService = estudioService;
		this.capacitacionService = capacitacionService;
		this.eventoService = eventoService;
		this.logroService = logroService;
		this.quienesSomosService = quienesSomosService;
		this.memoriaService = memoriaService;
		this.tagService = tagService;
		this.context = context;
	}

	@GetMapping(value = "")
	public ModelAndView vistaInicio(Model model) {
		model.addAttribute("sliders", sliderService.findAllByFlagActivo(true));
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("array1", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_2")));
		model.addAttribute("array2", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_TRAININGS")));
		model.addAttribute("memory", resumenService.getResumenById(2));
		return new ModelAndView(ViewConstant.EN_INICIO);
	}
	
	@GetMapping(value = "/research/{titulo:.+}/{contenidoWebId}")
	public ModelAndView vistaTemplateEstudio(
								@PathVariable(name = "contenidoWebId") String contenidoWebId,
								Model model) {
		try {
			int cwId = Integer.parseInt(contenidoWebId); 
			EnEstudio estudio = estudioService.getEstudioByContenidoWebId(cwId);
			if(estudio != null) {
			model.addAttribute("estudio", estudio);
			model.addAttribute("images", contenidoImagenService.findByContenidoWebId(cwId));
			model.addAttribute("files", contenidoArchivoService.findByContenidoWebId(cwId));
			return new ModelAndView(ViewConstant.EN_TEMPLATE_ESTUDIO);
			}
			return new ModelAndView(ViewConstant.ERROR404);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return new ModelAndView(ViewConstant.ERROR500);
		}
		
	}
	
	@GetMapping(value = "/training/{titulo:.+}/{contenidoWebId}")
	public ModelAndView vistaTemplateCapacitacion(
								@PathVariable(name = "contenidoWebId") String contenidoWebId,
								Model model) {
		try {
			int cwId = Integer.parseInt(contenidoWebId); 
			EnCapacitacion capacitacion = capacitacionService.getCapacitacionByContenidoWebId(cwId);
			if(capacitacion != null) {

				model.addAttribute("capacitacion", capacitacion);
				model.addAttribute("images", contenidoImagenService.findByContenidoWebId(cwId));
				model.addAttribute("files", contenidoArchivoService.findByContenidoWebId(cwId));
				return new ModelAndView(ViewConstant.EN_TEMPLATE_CAPACITACION);
			}
			return new ModelAndView(ViewConstant.ERROR404);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return new ModelAndView(ViewConstant.ERROR500);
		}
	}
	
	@GetMapping(value = "/event/{titulo:.+}/{contenidoWebId}")
	public ModelAndView vistaTemplateEvento(
								@PathVariable(name = "contenidoWebId") String contenidoWebId,
								Model model) {
		try {
			int cwId = Integer.parseInt(contenidoWebId); 
			EnEvento evento = eventoService.getEventoByContenidoWebId(cwId);
			if(evento != null) {
				model.addAttribute("evento", evento);
				model.addAttribute("images", contenidoImagenService.findByContenidoWebId(cwId));
				model.addAttribute("files", contenidoArchivoService.findByContenidoWebId(cwId));
				return new ModelAndView(ViewConstant.EN_TEMPLATE_EVENTO);
			}
			return new ModelAndView(ViewConstant.ERROR404);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return new ModelAndView(ViewConstant.ERROR500);
		}
	}

	@GetMapping(value = "/research/get/dates")
	public @ResponseBody List<Date>  getAllResearchDates() {
		return estudioService.findDistinctFechaEstudio();
	}
	
	@GetMapping(value = "/research/search")
	public ModelAndView vistaBusquedaEstudio(
								Model model) {
		List<EnContenidoWeb> lstContenidoWeb = contenidoWebService.findAllByTipoContenidoId(1);
		model.addAttribute("lstEstudios", lstContenidoWeb);
		return new ModelAndView(ViewConstant.MAIN_ESTUDIO_SEARCH);
	}
	
	@GetMapping(value = "/trainings/search")
	public ModelAndView vistaBusquedaCapacitacion(
								Model model) throws ParseException {
		List<EnContenidoWeb> lstContenidoWeb = contenidoWebService.findAllByTipoContenidoId(2);
		model.addAttribute("lstCapacitaciones", lstContenidoWeb);
		return new ModelAndView(ViewConstant.MAIN_CAPACITACION_SEARCH);
	}

	@GetMapping(value = "/trainings/get/dates")
	public @ResponseBody List<Date> getAllTrainingsDates() throws ParseException {
		return capacitacionService.findDistinctFechaCapacitacion();
	}
	
	@GetMapping(value = "/events/search")
	public ModelAndView vistaBusquedaEvento(
								Model model) {
		List<EnContenidoWeb> lstContenidoWeb = contenidoWebService.findWithResumenByTipoContenidoId(3);
		model.addAttribute("lstEventos", lstContenidoWeb);
		return new ModelAndView(ViewConstant.MAIN_EVENTO_SEARCH);
	}

	@GetMapping(value = "/events/get/dates")
	public @ResponseBody List<Date>  getAllEventDates() {
		return eventoService.findDistinctFechaEvento();
	}
	
	@GetMapping(value = "/achievements/{beneficiario}")
	public ModelAndView vistaBusquedaLogro(
								@PathVariable String beneficiario, Model model) {
		List<EnLogro> lstLogro = null;
		int beneficiarioId = 0;
		String msg = "";
		switch (beneficiario) {
		case "minem":
			lstLogro = logroService.findAllByBeneficiarioId(1);
			beneficiarioId = 1;
			msg = "The events presented below counted with the participation of the PROSEMER programme, supporting through many activities or estudies programmed and executed in coordination with MINEM:";
			break;
		case "osinergmin":
			lstLogro = logroService.findAllByBeneficiarioId(2);
			beneficiarioId = 2;
			msg = "The events presented below counted with the participation of the PROSEMER programme, supporting through many activities or estudies programmed and executed in coordination with OSINERGMIN:”";
			break;
		case "fonafe":
			lstLogro = logroService.findAllByBeneficiarioId(3);
			beneficiarioId = 3;
			msg = "In coordination with FONAFE, PROSEMER participated basically in the development of proposals for facilitating the implementation of Good Corporate Governance (BGC), and in training activities for strengthening FONAFE staff on BGC issues. Then, the proposals developed as regulations, methodologies and automated tools are subject to the approval or implementation of FONAFE. Shown below are the progress of the approval or implementation of proposals, as well as other relevant activities:";
			break;
		case "petroperu":
			lstLogro = logroService.findAllByBeneficiarioId(4);
			beneficiarioId = 4;
			msg = "In coordination with PETROPERU, PROSEMER participated basically in the development of proposals for facilitating the implementation of Good Corporate Governance (BGC), and in training activities for strengthening PETROPERU staff on BGC issues. Then, the proposals developed as regulations, methodologies and automated tools are subject to the approval or implementation of PETROPERU. Shown below are the progress of the approval or implementation of proposals, as well as other relevant activities:";
			break;
		default:
			return new ModelAndView(ViewConstant.ERROR404);
		}
		model.addAttribute("lstLogros", lstLogro);
		//GET ALL IDS
		List<Integer> lstEstudioIds = logroService.findAllByEstudioIdByBeneficiarioId(beneficiarioId);
		lstEstudioIds.removeAll(Collections.singleton(null));
		if(lstEstudioIds != null && lstEstudioIds.size() > 0)			
			model.addAttribute("lstEstudios", estudioService.findAllByIdsIn(lstEstudioIds));
		model.addAttribute("beneficiario", beneficiario.toUpperCase());
		model.addAttribute("msgCabecera", msg);
		return new ModelAndView(ViewConstant.MAIN_LOGRO_SEARCH);
	}
	
	@GetMapping(value = "/research/{beneficiario}")
	public ModelAndView vistaBusquedaEstudioPorBeneficiario(
								@PathVariable String beneficiario, Model model) {
		int beneficiarioId = 0;

		switch (beneficiario) {
		case "minem":
			beneficiarioId = 1;
			break;
		case "osinergmin":
			beneficiarioId = 2;
			break;
		case "fonafe":
			beneficiarioId = 3;
			break;
		case "petroperu":
			beneficiarioId = 4;
			break;
		default:
			return new ModelAndView(ViewConstant.ERROR404);
		}
		List<EnContenidoWeb> lstContenidoWeb;
		List<Integer> lstEstudioIds = esEstudioService.findAllIdsByFlagActivoAndBeneficiarioId(true, beneficiarioId);
		model.addAttribute("beneficiario", beneficiario.toUpperCase());

		if(lstEstudioIds.isEmpty()) {
			return new ModelAndView(ViewConstant.SEC_ESTUDIO_SEARCH);
		}else {
			lstContenidoWeb = contenidoWebService.findAllByIdIn(lstEstudioIds);	
		}

		model.addAttribute("lstEstudios", lstContenidoWeb);
		model.addAttribute("beneficiarioId", beneficiarioId);
		return new ModelAndView(ViewConstant.SEC_ESTUDIO_SEARCH);
	}

	@GetMapping(value = "/research/get/dates/sec/{beneficiarioId}")
	public @ResponseBody List<Date>  getAllResearchDatesByBeneficiarioId(@PathVariable String beneficiarioId) {
		return estudioService.findDistinctFechaEstudioByBeneficiarioId(Integer.parseInt(beneficiarioId));
	}
	
	@GetMapping(value = "/history")
	public ModelAndView vistaHistoria(
								Model model) {
		model.addAttribute("historia", quienesSomosService.findOneById(1));
		model.addAttribute("images", contenidoImagenService.findByContenidoWebId(quienesSomosService.findContenidoIdById(1)));
		return new ModelAndView(ViewConstant.EN_MAIN_HISTORIA);
	}
	
	@GetMapping(value = "/objectives")
	public ModelAndView vistaObjetivos(
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("objetivo", quienesSomosService.findOneById(2));
		return new ModelAndView(ViewConstant.EN_MAIN_OBJETIVOS);
	}
	
	@GetMapping(value = "/organizational-structure")
	public ModelAndView vistaEstructuraOrganizacional(
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("estructura", quienesSomosService.findOneById(3));
		return new ModelAndView(ViewConstant.EN_MAIN_ESTRUCTURA_ORGANIZACIONAL);
	}
	
	@GetMapping(value = "/legal-framework")
	public ModelAndView vistaMarcoLegal(
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("marco", quienesSomosService.findOneById(4));
		return new ModelAndView(ViewConstant.EN_MAIN_MARCOLEGAL);
	}
	
	@GetMapping(value = "/program-memory")
	public ModelAndView vistaMemoriaDelPrograma(//Memoria del proyecto == Memoria del programa
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("memoria", memoriaService.findOneById(1));
		return new ModelAndView(ViewConstant.MAIN_PROJECT_MEMORY);
	}
	
	@GetMapping(value = "/gt/estudio")
	public @ResponseBody List<EnContenidoWeb> consultarEstudios(
							@RequestParam(required = false) String titulo, 
							@RequestParam(required = false) String fecha,
							@RequestParam(required = false) String tags,
							@RequestParam(required = false) Integer beneficiarioId) throws ParseException {
		//TipoContenido 1 > EnEstudio
		List<EnContenidoWeb> lstContenidoWeb = null;
		Date fechaEstudio = null;
		if(fecha != null) {
			fechaEstudio = Parseador.fromStringToDate(fecha);
			if(fechaEstudio != null) {
				List<Integer> lst = null;
				if(beneficiarioId == null) {
					lst = estudioService.findAllIdsByFechaEstudio(fechaEstudio);
				}else{
					lst = estudioService.findAllIdsByFechaEstudioAndBeneficiarioId(fechaEstudio, beneficiarioId);
				}
				lstContenidoWeb = contenidoWebService.findAllByIdIn(lst);
			}
		}else if(tags != null) {
			lstContenidoWeb = contenidoWebService.findAllTagsByTipoContenidoId(1);
			List<EnContenidoWeb> lstMatch = new ArrayList<>();
			for(EnContenidoWeb x: lstContenidoWeb) {
				if(x.getTags() == null) {}else {
					if(Parseador.removeDiacriticalMarks(x.getTags().toLowerCase()).contains(Parseador.removeDiacriticalMarks(tags.toLowerCase()))) {
						lstMatch.add(x);
					}
				}
			 }
			return lstMatch;
		}else {
			if(titulo == null) {
				lstContenidoWeb = contenidoWebService.findAllByTipoContenidoId(1);
			}else {
				lstContenidoWeb = contenidoWebService.findAllByTipoContenidoIdAndTitulo(1, titulo);	
			}
		}
		
		return lstContenidoWeb;
	}

	@GetMapping(value = "/gt/estudio/tags")
	public @ResponseBody List<String> consultarTagsEstudios() {
		return estudioService.findAllDistinctTags();
	}
	
	@GetMapping(value = "/gt/capacitacion")
	public @ResponseBody List<EnContenidoWeb> consultarCapacitaciones(
							@RequestParam String titulo, 
							@RequestParam(required = false) String fecha,
							@RequestParam(required = false) String tags) throws ParseException {
		
		List<EnContenidoWeb> lstContenidoWeb = null;
		Date fechaCapacitacion = null;
		if(fecha != null) {
			fechaCapacitacion = Parseador.fromStringToDate(fecha);
			if(fechaCapacitacion != null) {
				List<Integer> lst = capacitacionService.findAllIdsByFechaCapacitacion(fechaCapacitacion);
				lstContenidoWeb = contenidoWebService.findAllByIdIn(lst);
			}
		}else if(tags != null) {
			lstContenidoWeb = contenidoWebService.findAllTagsByTipoContenidoId(2);
			List<EnContenidoWeb> lstMatch = new ArrayList<>();
			for(EnContenidoWeb x: lstContenidoWeb) {
				if(x.getTags() == null) {}else {
					if(Parseador.removeDiacriticalMarks(x.getTags().toLowerCase()).contains(Parseador.removeDiacriticalMarks(tags.toLowerCase()))) {
						lstMatch.add(x);
					}
				}
			 }
			return lstMatch;
		}else {
			if(titulo == null) {
				lstContenidoWeb = contenidoWebService.findAllByTipoContenidoId(2);
			}else {
				lstContenidoWeb = contenidoWebService.findAllByTipoContenidoIdAndTitulo(2, titulo);	
			}
		}
		
		return lstContenidoWeb;
	}

	@GetMapping(value = "/gt/capacitacion/tags")
	public @ResponseBody List<String> consultarCapacitaciones() {
		return capacitacionService.findAllDistinctTags();
	}
	
	@GetMapping(value = "/gt/evento")
	public @ResponseBody List<EnContenidoWeb> consultarEventos(
							@RequestParam String titulo, 
							@RequestParam(required = false) String fecha,
							@RequestParam(required = false) String tags) throws ParseException {
		List<EnContenidoWeb> lstContenidoWeb = null;
		Date fechaEvento = null;
		if(fecha != null) {
			fechaEvento = Parseador.fromStringToDate(fecha);
			if(fechaEvento != null) {
				List<Integer> lst = eventoService.findAllIdsByFechaEvento(fechaEvento);
				lstContenidoWeb = contenidoWebService.findWithResumenAllByIdIn(lst);
			}
		}else if(tags != null) {
			lstContenidoWeb = contenidoWebService.findWithResumenTagsByTipoContenidoId(3);
			List<EnContenidoWeb> lstMatch = new ArrayList<>();
			for(EnContenidoWeb x: lstContenidoWeb) {
				if(x.getTags() == null) {}else {
					if(Parseador.removeDiacriticalMarks(x.getTags().toLowerCase()).contains(Parseador.removeDiacriticalMarks(tags.toLowerCase()))) {
						lstMatch.add(x);
					}
				}
			 }
			return lstMatch;
		}else {
			lstContenidoWeb = contenidoWebService.findWithResumenByTipoContenidoIdAndTitulo(3, titulo);	
		}
		
		return lstContenidoWeb;
	}

	@GetMapping(value = "/gt/evento/tags")
	public @ResponseBody List<String> consultarTagsEventos() {
		return eventoService.findAllDistinctTags();
	}
	
	@GetMapping(value = "/gt/logro")
	public @ResponseBody List<EnLogro> consultarLogros(
							@RequestParam int estudioId) {
		return logroService.findAllByEstudioId(estudioId);
	}
}
