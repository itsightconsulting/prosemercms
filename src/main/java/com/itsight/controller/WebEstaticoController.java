package com.itsight.controller;

import com.itsight.constants.Parseador;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.*;
import com.itsight.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/web")

public class WebEstaticoController {

	public static final Logger LOGGER = LogManager.getLogger(WebEstaticoController.class);
		
	private ContenidoWebService contenidoWebService;
	
	private SliderService sliderService;
	
	private ResumenService resumenService;
	
	private ContenidoArchivoService contenidoArchivoService;
	
	private ContenidoImagenService contenidoImagenService;
	
	private EstudioService estudioService;
	
	private CapacitacionService capacitacionService;
	
	private EventoService eventoService;
	
	private LogroService logroService;
	
	private QuienesSomosService quienesSomosService;
	
	private MemoriaService memoriaService;
	
	private TagService tagService;
	
	
	private ServletContext context;
	
	@Autowired
	public WebEstaticoController(
						ContenidoWebService contenidoWebService,
						SliderService sliderService,
						ResumenService resumenService,
						ContenidoArchivoService contenidoArchivoService,
						ContenidoImagenService contenidoImagenService,
						EstudioService estudioService,
						CapacitacionService capacitacionService,
						EventoService eventoService,
						LogroService logroService,
						QuienesSomosService quienesSomosService,
						TagService tagService,
						MemoriaService memoriaService,
						ServletContext context) {
		this.contenidoWebService = contenidoWebService;
		this.sliderService = sliderService;
		this.resumenService = resumenService;
		this.contenidoArchivoService = contenidoArchivoService;
		this.contenidoImagenService = contenidoImagenService;
		this.estudioService = estudioService;
		this.capacitacionService = capacitacionService;
		this.eventoService = eventoService;
		this.logroService = logroService;
		this.quienesSomosService = quienesSomosService;
		this.memoriaService = memoriaService;
		this.tagService = tagService;
		this.context = context;
	}

	@GetMapping(value = "/inicio")
	public ModelAndView vistaInicio(Model model) {
		model.addAttribute("sliders", sliderService.findAllByFlagActivo(true));
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("array1", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_2")));
		model.addAttribute("array2", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_TRAININGS")));
		model.addAttribute("memoria", resumenService.getResumenById(2));
		return new ModelAndView(ViewConstant.INICIO);
	}
	
	@GetMapping(value = "/estudio/{titulo:.+}/{contenidoWebId}")
	public ModelAndView vistaTemplateEstudio(
								@PathVariable(name = "contenidoWebId") String contenidoWebId,
								Model model) {
		try {
			int cwId = Integer.parseInt(contenidoWebId); 
			Estudio estudio = estudioService.getEstudioByContenidoWebId(cwId);
			if(estudio != null) {
				model.addAttribute("estudio", estudio);
				model.addAttribute("images", contenidoImagenService.findByContenidoWebId(cwId));
				model.addAttribute("files", contenidoArchivoService.findByContenidoWebId(cwId));
				return new ModelAndView(ViewConstant.TEMPLATE_ESTUDIO);
			}
			return new ModelAndView(ViewConstant.ERROR404);
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return new ModelAndView(ViewConstant.ERROR500);
		}
		
	}
	
	@GetMapping(value = "/capacitacion/{titulo:.+}/{contenidoWebId}")
	public ModelAndView vistaTemplateCapacitacion(
								@PathVariable(name = "contenidoWebId") String contenidoWebId,
								Model model) {
		try {
			int cwId = Integer.parseInt(contenidoWebId); 
			Capacitacion capacitacion = capacitacionService.getCapacitacionByContenidoWebId(cwId);
			if(capacitacion != null) {
				model.addAttribute("capacitacion", capacitacion);
				model.addAttribute("images", contenidoImagenService.findByContenidoWebId(cwId));
				model.addAttribute("files", contenidoArchivoService.findByContenidoWebId(cwId));
				return new ModelAndView(ViewConstant.TEMPLATE_CAPACITACION);
			}
			return new ModelAndView(ViewConstant.ERROR404);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return new ModelAndView(ViewConstant.ERROR500);
		}
	}
	
	@GetMapping(value = "/evento/{titulo:.+}/{contenidoWebId}")
	public ModelAndView vistaTemplateEvento(
								@PathVariable(name = "contenidoWebId") String contenidoWebId,
								Model model) {
		try {
			int cwId = Integer.parseInt(contenidoWebId); 
			Evento evento = eventoService.getEventoByContenidoWebId(cwId);
			if(evento != null) {
				model.addAttribute("evento", evento);
				model.addAttribute("images", contenidoImagenService.findByContenidoWebId(cwId));
				model.addAttribute("files", contenidoArchivoService.findByContenidoWebId(cwId));
				return new ModelAndView(ViewConstant.TEMPLATE_EVENTO);
			}
			return new ModelAndView(ViewConstant.ERROR404);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return new ModelAndView(ViewConstant.ERROR500);
		}
		
	}
	
	@GetMapping(value = "/estudio/busqueda")
	public ModelAndView vistaBusquedaEstudio(
								Model model) {
		List<ContenidoWeb> lstContenidoWeb = contenidoWebService.findAllByTipoContenidoId(1);
		model.addAttribute("lstEstudios", lstContenidoWeb);
		model.addAttribute("lstEstudioFecha", estudioService.findDistinctFechaEstudio());
		return new ModelAndView(ViewConstant.MAIN_ESTUDIO_BUSQUEDA);
	}
	
	@GetMapping(value = "/capacitacion/busqueda")
	public ModelAndView vistaBusquedaCapacitacion(
								Model model) throws ParseException {
		List<ContenidoWeb> lstContenidoWeb = contenidoWebService.findAllByTipoContenidoId(2);
		model.addAttribute("lstCapacitaciones", lstContenidoWeb);
		model.addAttribute("lstCapacitacionFecha", capacitacionService.findDistinctFechaCapacitacion());
		return new ModelAndView(ViewConstant.MAIN_CAPACITACION_BUSQUEDA);
	}
	
	@GetMapping(value = "/evento/busqueda")
	public ModelAndView vistaBusquedaEvento(
								Model model) {
		List<ContenidoWeb> lstContenidoWeb = contenidoWebService.findWithResumenByTipoContenidoId(3);
		model.addAttribute("lstEventos", lstContenidoWeb);
		model.addAttribute("lstEventoFecha", eventoService.findDistinctFechaEvento());
		model.addAttribute("lstTag", tagService.listAllByTipoTag(1));//ES
		return new ModelAndView(ViewConstant.MAIN_EVENTO_BUSQUEDA);
	}
	
	@GetMapping(value = "/logros/{beneficiario}")
	public ModelAndView vistaBusquedaLogro(
								@PathVariable String beneficiario, Model model) {
		List<Logro> lstLogro = null;
		int beneficiarioId = 0;
		String msg = "";
		switch (beneficiario) {
		case "minem":
			lstLogro = logroService.findAllByBeneficiarioId(1);
			beneficiarioId = 1;
			break;
		case "osinergmin":
			lstLogro = logroService.findAllByBeneficiarioId(2);
			beneficiarioId = 2;
			break;
		case "fonafe":
			lstLogro = logroService.findAllByBeneficiarioId(3);
			beneficiarioId = 3;
			msg = "En coordinación con FONAFE, la participación del PROSEMER se basó en el desarrollo de propuestas que faciliten la implementación del Buen Gobierno Corporativo (BGC), y de actividades de capacitación para fortalecer al personal de FONAFE en temas de BGC. Posteriormente, las propuestas desarrolladas como normativas, metodologías y herramientas automatizas, están sujetos a la aprobación o implementación de FONAFE. Los avances de esta aprobación o implementación, así como otras actividades relevantes desarrolladas por el PROSEMER son las siguientes:";
			break;
		case "petroperu":
			lstLogro = logroService.findAllByBeneficiarioId(4);
			beneficiarioId = 4;
			msg = "En coordinación con PETROPERU, la participación del PROSEMER se basó en el desarrollo de propuestas que faciliten la implementación del Buen Gobierno Corporativo (BGC), y de actividades de capacitación para fortalecer al personal de PETROPERU en temas de BGC. Posteriormente, las propuestas desarrolladas como normativas, metodologías y herramientas automatizas, están sujetos a la aprobación o implementación de PETROPERU. Los avances de esta aprobación o implementación, así como otras actividades relevantes desarrolladas por el PROSEMER son las siguientes:";
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
		return new ModelAndView(ViewConstant.MAIN_LOGRO_BUSQUEDA);
	}
	
	@GetMapping(value = "/estudios/{beneficiario}")
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
		List<ContenidoWeb> lstContenidoWeb = null;
		List<Integer> lstEstudioIds = estudioService.findAllIdsByFlagActivoAndBeneficiarioId(true, beneficiarioId);
		model.addAttribute("beneficiario", beneficiario.toUpperCase());

		if(lstEstudioIds.isEmpty()) {
			return new ModelAndView(ViewConstant.SEC_ESTUDIO_BUSQUEDA);
		}else {
			lstContenidoWeb = contenidoWebService.findAllByIdIn(lstEstudioIds);	
		}

		model.addAttribute("lstEstudios", lstContenidoWeb);
		model.addAttribute("lstEstudioFecha", estudioService.findDistinctFechaEstudioByBeneficiarioId(beneficiarioId));
		model.addAttribute("beneficiarioId", beneficiarioId);
		return new ModelAndView(ViewConstant.SEC_ESTUDIO_BUSQUEDA);
	}
	
	@GetMapping(value = "/historia")
	public ModelAndView vistaHistoria(
								Model model) {
		model.addAttribute("historia", quienesSomosService.findOneById(1));
		model.addAttribute("images", contenidoImagenService.findByContenidoWebId(quienesSomosService.findContenidoIdById(1)));
		return new ModelAndView(ViewConstant.MAIN_HISTORIA);
	}
	
	@GetMapping(value = "/objetivos")
	public ModelAndView vistaObjetivos(
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("objetivo", quienesSomosService.findOneById(2));
		return new ModelAndView(ViewConstant.MAIN_OBJETIVOS);
	}
	
	@GetMapping(value = "/estructura-organizacional")
	public ModelAndView vistaEstructuraOrganizacional(
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("estructura", quienesSomosService.findOneById(3));
		return new ModelAndView(ViewConstant.MAIN_ESTRUCTURA_ORGANIZACIONAL);
	}
	
	@GetMapping(value = "/marco-legal")
	public ModelAndView vistaMarcoLegal(
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("marco", quienesSomosService.findOneById(4));
		return new ModelAndView(ViewConstant.MAIN_MARCOLEGAL);
	}
	
	@GetMapping(value = "/memoria-del-programa")
	public ModelAndView vistaMemoriaDelPrograma(//Memoria del proyecto == Memoria del programa
								Model model) {
		model.addAttribute("array", resumenService.findAllByIdIn((Integer[]) context.getAttribute("INDEX_PAGE_STUDIES_1")));
		model.addAttribute("memoria", memoriaService.findOneById(1));
		return new ModelAndView(ViewConstant.MAIN_MEMORIA_DEL_PROYECTO);
	}
	
	@GetMapping(value = "/gt/estudio")
	public @ResponseBody List<ContenidoWeb> consultarEstudios(
							@RequestParam(required = false) String titulo, 
							@RequestParam(required = false) String fecha,
							@RequestParam(required = false) String tags,
							@RequestParam(required = false) Integer beneficiarioId) throws ParseException {
		//TipoContenido 1 > Estudio
		List<ContenidoWeb> lstContenidoWeb = null;
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
			List<ContenidoWeb> lstMatch = new ArrayList<>();
			for(ContenidoWeb x: lstContenidoWeb) {
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
	public @ResponseBody List<ContenidoWeb> consultarCapacitaciones(
			@RequestParam String titulo,
			@RequestParam(required = false) String fecha,
			@RequestParam(required = false) String tags) throws ParseException {

		List<ContenidoWeb> lstContenidoWeb = null;
		Date fechaCapacitacion;
		if(fecha != null) {
			fechaCapacitacion = Parseador.fromStringToDate(fecha);
			if(fechaCapacitacion != null) {
				List<Integer> lst = capacitacionService.findAllIdsByFechaCapacitacion(fechaCapacitacion);
				lstContenidoWeb = contenidoWebService.findAllByIdIn(lst);
			}
		}else if(tags != null) {
			lstContenidoWeb = contenidoWebService.findAllTagsByTipoContenidoId(2);
			List<ContenidoWeb> lstMatch = new ArrayList<>();
			for(ContenidoWeb x: lstContenidoWeb) {
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
	public @ResponseBody List<String> consultarTagsCapacitaciones() {
		return capacitacionService.findAllDistinctTags();
	}
	
	@GetMapping(value = "/gt/evento")
	public @ResponseBody List<ContenidoWeb> consultarEventos(
							@RequestParam String titulo, 
							@RequestParam(required = false) String fecha,
							@RequestParam(required = false) String tags) throws ParseException {
		List<ContenidoWeb> lstContenidoWeb = null;
		Date fechaEvento = null;
		if(fecha != null) {
			fechaEvento = Parseador.fromStringToDate(fecha);
			if(fechaEvento != null) {
				List<Integer> lst = eventoService.findAllIdsByFechaEvento(fechaEvento);
				lstContenidoWeb = contenidoWebService.findWithResumenAllByIdIn(lst);
			}
		}else if(tags != null) {
			lstContenidoWeb = contenidoWebService.findWithResumenTagsByTipoContenidoId(3);
			List<ContenidoWeb> lstMatch = new ArrayList<>();
			for(ContenidoWeb x: lstContenidoWeb) {
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
	public @ResponseBody List<Logro> consultarLogros(
							@RequestParam int estudioId) {
		return logroService.findAllByEstudioId(estudioId);
	}
}
