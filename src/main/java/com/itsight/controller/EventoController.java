package com.itsight.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.Utilitarios;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.ContenidoArchivo;
import com.itsight.domain.ContenidoImagen;
import com.itsight.domain.ContenidoWeb;
import com.itsight.domain.Evento;
import com.itsight.domain.Resumen;
import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.domain.en.EnEvento;
import com.itsight.domain.en.EnResumen;
import com.itsight.service.ContenidoArchivoService;
import com.itsight.service.ContenidoImagenService;
import com.itsight.service.ContenidoWebService;
import com.itsight.service.EventoService;
import com.itsight.service.ResumenService;
import com.itsight.service.UsuarioService;
import com.itsight.service.en.EnContenidoWebService;
import com.itsight.service.en.EnEventoService;

@Controller
@RequestMapping("/gestion/evento")
public class EventoController {
	
	private static final Logger LOGGER = LogManager.getLogger(EventoController.class);
	
	private EnEventoService enEventoService;
	
	private EventoService eventoService;
				
	private ContenidoWebService contenidoWebService;
	
	private ContenidoArchivoService contenidoArchivoService;
	
	private ContenidoImagenService contenidoImagenService;
	
	private UsuarioService usuarioService;
	
	private ResumenService resumenService;
	
	//E N G L I S H  S E R V I C E S
	private EnContenidoWebService enContenidoWebService;
	
	private String mainRoute;
	
    @Autowired
	public EventoController(
			EnEventoService enEventoService,
			EventoService eventoService, 
			ContenidoWebService contenidoWebService, 
			ContenidoArchivoService contenidoArchivoService, 
			ContenidoImagenService contenidoImagenService, 
			UsuarioService usuarioService,
			ResumenService resumenService,
			EnContenidoWebService enContenidoWebService,
			String mainRoute) {
    	this.enEventoService = enEventoService;
		this.eventoService = eventoService;
		this.contenidoWebService = contenidoWebService;
		this.contenidoArchivoService = contenidoArchivoService;
		this.contenidoImagenService = contenidoImagenService;
		this.usuarioService = usuarioService;
		this.resumenService = resumenService;
		this.enContenidoWebService = enContenidoWebService;
		this.mainRoute = mainRoute;
	}
    
    @GetMapping(value = "")
	public ModelAndView principalEvento(Model  model) {
		return new ModelAndView(ViewConstant.MAIN_EVENTO);
	}
    
	@GetMapping(value = "/nuevo")
	public ModelAndView formularioEvento(Model model) {
		model.addAttribute("type", "evento");
		return new ModelAndView(ViewConstant.MAIN_EVENTO_NUEVO);
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addEvent(
				@ModelAttribute Evento evento, 
				@RequestParam boolean flagResumen,
				@RequestParam(value="resumen", required = false) String txtResumen) {
			
		ContenidoWeb contenidoWeb = null;
		EnContenidoWeb eContenidoWeb = null;
		
		if(evento.getId()> 0) {
			Evento qEvento = eventoService.findOneById(evento.getId());
			qEvento.setTituloPrincipal(evento.getTituloPrincipal());
			qEvento.setTituloLargo(evento.getTituloLargo());
			qEvento.setDescripcion(evento.getDescripcion());
			qEvento.setFechaEvento(evento.getFechaEvento());
			qEvento.setTags(evento.getTags());
			contenidoWeb = contenidoWebService.findOneById(qEvento.getContenidoWeb().getId());
			contenidoWeb.setTitulo(qEvento.getTituloPrincipal());
			contenidoWeb.setUrl(3, qEvento.getTituloPrincipal().toLowerCase().replace(" ", "-"));
			contenidoWeb.setTags(evento.getTags());
			contenidoWeb.addEvento(qEvento);
			//Resumen
			contenidoWeb.getResumen().setResumen(txtResumen);
			contenidoWeb.getResumen().setTitulo(contenidoWeb.getTitulo());
			contenidoWeb.getResumen().setUrl(contenidoWeb.getUrl());
			contenidoWebService.add(contenidoWeb);

		}else {// R E G I S T R A R 
			contenidoWeb = new ContenidoWeb();
			//Evento
			evento.setFlagActivo(true);
			evento.setFechaCreacion();
			
			//Agregando Contenido Web
			contenidoWeb.setTitulo(evento.getTituloPrincipal());
			contenidoWeb.setTags(evento.getTags());
			contenidoWeb.setUrl(3, evento.getTituloPrincipal().toLowerCase().replace(" ", "-"));
			contenidoWeb.setFechaCreacion();
			contenidoWeb.setFechaContenido(evento.getFechaEvento());
			contenidoWeb.setFlagActivo(true);
			contenidoWeb.setFlagResumen(flagResumen);
			contenidoWeb.setUsuario(usuarioService.findIdByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
			contenidoWeb.setTipoContenido(3);
			
			//Copying
			EnEvento eEvento = new EnEvento();
			BeanUtils.copyProperties(evento, eEvento);
			
			//Copying
			eContenidoWeb = new EnContenidoWeb();
			BeanUtils.copyProperties(contenidoWeb, eContenidoWeb);
			
			//Target
			eContenidoWeb.addEnEvento(eEvento);
			contenidoWeb.addEvento(evento);
			
			//Creando resumen
			if(flagResumen) {
				Resumen resumen = new Resumen();
				resumen.setResumen(txtResumen);
				
				EnResumen eResumen = new EnResumen();
				BeanUtils.copyProperties(resumen, eResumen);
				//Target
				eContenidoWeb.addResumen(eResumen);
				contenidoWeb.addResumen(resumen);
			}
			//Enviando a BD | Por transferencia de estados(Cascade) se registrará tanto el contenido como el evento
			contenidoWebService.add(contenidoWeb);
			enContenidoWebService.add(eContenidoWeb);
		}
		
		
		return String.valueOf(contenidoWeb.getId());
		
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody Evento getEventoById(@RequestParam(value = "id") int eventoId) {
		return eventoService.findOneWithContenidoWebById(eventoId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}")
	public @ResponseBody List<Evento> listAllEvents(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado){
			
    	List<Evento> lstEvento;
			if(comodin.equals("0") && estado.equals("-1")) {
				lstEvento = eventoService.listAll();
			}else {
				if(comodin.equals("0") && !estado.equals("-1")) {
					lstEvento = eventoService.findAllByFlagActivo(Boolean.valueOf(estado));
				}else {
					comodin = comodin.equals("0") ? "":comodin;
					lstEvento = eventoService.findAllByTituloPrincipalContainingIgnoreCase(comodin);
					if(!estado.equals("-1")) {
						List<Evento> lstEventoFilter = new ArrayList<>();

						for (Evento x : lstEvento) {
							if(Boolean.valueOf(estado).equals(x.isFlagActivo())) {
							lstEvento.add(x);
							}
						}
						return lstEventoFilter;
					}
				}
			}
		return lstEvento;
    }
	
	@PutMapping(value = "/desactivar")
	public @ResponseBody String disabled(@RequestParam(value = "id") int id) {
			Evento evento = eventoService.findOneById(id);

			if(evento.isFlagActivo()) {
				evento.setFlagActivo(false);
			}else {
				evento.setFlagActivo(true);
			}
			evento.setFechaModificacion();
			eventoService.update(evento);
			enEventoService.updateStatusById(evento.isFlagActivo(), id);
			//Actualizando el flag de la tabla contenido web
			contenidoWebService.updatingFlagActivoById(evento.getContenidoWeb().getId(), evento.isFlagActivo());
			enContenidoWebService.updatingFlagActivoById(evento.getContenidoWeb().getId(), evento.isFlagActivo());
			return "1";
	}
	
	@GetMapping(value = "/obtenerArchivos")
	public @ResponseBody List<ContenidoArchivo> obtenerArchivos(@RequestParam(value = "eventoId") int eventoId) {
		return contenidoArchivoService.findAllByContenidoWebId(eventoService.findContenidoIdById(eventoId));
	}
	
	@GetMapping(value = "/obtenerImagenes")
	public @ResponseBody List<ContenidoImagen> obtenerImagenes(@RequestParam(value = "eventoId") int eventoId) {
		return contenidoImagenService.findAllByContenidoWebId(eventoService.findContenidoIdById(eventoId));
	}
	
	@RequestMapping(value = "/cargarArchivos", method = RequestMethod.POST)
	public @ResponseBody String guardarArchivo(
			@RequestPart(value = "fileImagePortada", required = false) MultipartFile fileImagePortada,
			@RequestPart(value = "fileImagenResumen", required = false) MultipartFile fileImagenResumen,
			@RequestParam(value = "Documentos", required = true) MultipartFile[] documentos,
			@RequestParam(value = "Imagenes", required = true) MultipartFile[] imagenes,
			@RequestParam(value = "ContenidoWebId") Integer contenidoWebId,
			@RequestParam(value = "Alias", required = true) String[] alias,
			@RequestParam(value = "SizeList", required = false) String[] sizes, HttpServletRequest request) throws JsonProcessingException {

		String rutaBase;
		if(alias.length == 0) {alias = new String[1];alias[0]="";}
		
		if (fileImagePortada != null) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Eventos/Imagenes/"+ contenidoWebId);
			guardarFile(fileImagePortada, contenidoWebId, null, rutaBase, 0, alias, sizes);
		}
		
		if (fileImagenResumen != null) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Eventos/Imagenes/"+ contenidoWebId);
			guardarFile(fileImagenResumen, contenidoWebId, null, rutaBase, -1, alias, sizes);
		}
		
		if(documentos.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Eventos/Archivos/"+ contenidoWebId);
			for (int i = 0; i < documentos.length; i++) {
				guardarFile(documentos[i], contenidoWebId, true, rutaBase, i, alias, sizes);
			}
		}
		
		if(imagenes.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Eventos/Imagenes/"+ contenidoWebId);
			for (int i = 0; i < imagenes.length; i++) {
				guardarFile(imagenes[i], contenidoWebId, false, rutaBase, i, alias, sizes);
			}
		}

		return "1";

	}
	
	private void guardarFile(MultipartFile file, int contenidoWebId, Boolean typeFile, String rutaBase, int i, String[] alias, String[] sizes) {
		if (!file.isEmpty()) {
			try {
				
				String[] splitNameFile = file.getOriginalFilename().split("\\.");
				String extension = "." + splitNameFile[splitNameFile.length - 1];
				String fullPath = "";
				UUID uuid = UUID.randomUUID(); 
				
				/*
				 * TypeFile:
				 * true: archivos
				 * false: imagenes
				 * null: imagenPortada
				 */
				
				fullPath = rutaBase + "/"+ uuid + extension;
				File nuevoFile = new File(fullPath);
				
				if(typeFile == null) {
					if(i == -1) {
						Resumen qResumen = resumenService.findResumenById(contenidoWebId);
						// Actualizando el evento
						qResumen.setRutaImagenPortada("/" + contenidoWebId + "/" + uuid + extension);
						qResumen.setNombreImagenPortada(file.getOriginalFilename());
						resumenService.update(qResumen);
					}
				}else {
					if(typeFile) {
						ContenidoArchivo contenidoArchivo = new ContenidoArchivo();
						contenidoArchivo.setAlias(alias[i].equals("") ? splitNameFile[0]:alias[i] );
						contenidoArchivo.setNombreMedia(alias[i].equals("") ? file.getOriginalFilename():alias[i]+extension);
						contenidoArchivo.setRealMediaPath(fullPath);
						contenidoArchivo.setRutaMediaWeb("/" + contenidoWebId + "/" + uuid + extension);
						contenidoArchivo.setUuid(uuid.toString());
						contenidoArchivo.setContenidoWeb(contenidoWebId);
						contenidoArchivo.setOrden(i);
						contenidoArchivo.setPeso(sizes[i]);
						
						contenidoArchivoService.add(contenidoArchivo);
					}else{
						
						ContenidoImagen contenidoImagen = new ContenidoImagen();
						contenidoImagen.setNombreMedia(file.getOriginalFilename());
						contenidoImagen.setRealMediaPath(fullPath);
						contenidoImagen.setRutaMediaWeb("/" + contenidoWebId + "/" + uuid + extension);
						contenidoImagen.setUuid(uuid.toString());
						contenidoImagen.setContenidoWeb(contenidoWebId);
						contenidoImagen.setOrden(i);
						contenidoImagenService.add(contenidoImagen);
					}
				}
				
				// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
				// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
				file.transferTo(nuevoFile);

				LOGGER.info("> ROUTE: " + fullPath);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("> Isn't a file");
		}
	}
	
	@RequestMapping(value = "/cargarArchivos/v2", method = RequestMethod.POST)
	public @ResponseBody String guardarArchivosSecundary(
			@RequestParam(value = "Documentos", required = false) MultipartFile[] documentos,
			@RequestParam(value = "Imagenes", required = false) MultipartFile[] imagenes,
			@RequestParam(value = "EventoId") Integer eventoId,
			@RequestParam(value = "Alias", required = true) String[] alias,
			@RequestParam(value = "SizeList", required = false) String[] sizes, HttpServletRequest request) throws JsonProcessingException {

		String rutaBase;
		int contenidoWebId = eventoService.findContenidoIdById(eventoId);
		if(alias.length == 0) {alias = new String[1];alias[0]="";}
		
		if(documentos.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Eventos/Archivos/"+ contenidoWebId);
			for (int i = 0; i < documentos.length; i++) {
				guardarFile(documentos[i], contenidoWebId, true, rutaBase, i, alias, sizes);
			}
		}
		
		if(imagenes.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Eventos/Imagenes/"+ contenidoWebId);
			for (int i = 0; i < imagenes.length; i++) {
				guardarFile(imagenes[i], contenidoWebId, false, rutaBase, i, alias, sizes);
			}
		}

		return "1";
	}
	
	@DeleteMapping(value = "/eliminar/{id}")
	public @ResponseBody String eliminar(@PathVariable(value = "id") int id){
		//Cascade
		eventoService.delete(id);
		enEventoService.delete(id);
		return "1";
	}
}