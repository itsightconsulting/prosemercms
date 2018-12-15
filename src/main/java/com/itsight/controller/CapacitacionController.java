package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
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
import com.itsight.constants.Parseador;
import com.itsight.constants.Utilitarios;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.Beneficiario;
import com.itsight.domain.Capacitacion;
import com.itsight.domain.ContenidoArchivo;
import com.itsight.domain.ContenidoImagen;
import com.itsight.domain.ContenidoWeb;
import com.itsight.domain.Parametro;
import com.itsight.domain.Resumen;
import com.itsight.domain.en.EnCapacitacion;
import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.domain.en.EnResumen;
import com.itsight.service.BeneficiarioService;
import com.itsight.service.CapacitacionService;
import com.itsight.service.ContenidoArchivoService;
import com.itsight.service.ContenidoImagenService;
import com.itsight.service.ContenidoWebService;
import com.itsight.service.ParametroService;
import com.itsight.service.ResumenService;
import com.itsight.service.TipoEstudioService;
import com.itsight.service.UsuarioService;
import com.itsight.service.en.EnCapacitacionService;
import com.itsight.service.en.EnContenidoWebService;
import com.itsight.service.en.EnResumenService;

@Controller
@RequestMapping("/gestion/capacitacion")
public class CapacitacionController {
	
	private static final Logger LOGGER = LogManager.getLogger(CapacitacionController.class);
	
	private EnCapacitacionService enCapacitacionService;
	
	private CapacitacionService capacitacionService;
	
	private ContenidoWebService contenidoWebService;
	
	private ContenidoArchivoService contenidoArchivoService;
	
	private ContenidoImagenService contenidoImagenService;
	
	private UsuarioService usuarioService;
	
	private TipoEstudioService tipoEstudioService;
	
	private ResumenService resumenService;
	
	private BeneficiarioService beneficiarioService;
	
	private ParametroService parametroService;
	
	private ServletContext context;
	
	//E N G L I S H  S E R V I C E S
	private EnContenidoWebService enContenidoWebService;
	private EnResumenService enResumenService;
	
	private String mainRoute;
	
    @Autowired
	public CapacitacionController(
			EnCapacitacionService enCapacitacionService,
			CapacitacionService capacitacionService,
			ContenidoWebService contenidoWebService, 
			ContenidoArchivoService contenidoArchivoService, 
			ContenidoImagenService contenidoImagenService, 
			UsuarioService usuarioService,
			TipoEstudioService tipoEstudioService,
			ResumenService resumenService,
			BeneficiarioService beneficiarioService,
			ParametroService parametroService,
			EnContenidoWebService enContenidoWebService,
			EnResumenService enResumenService,
			String mainRoute,
			ServletContext context) {
    	this.enCapacitacionService = enCapacitacionService;
    	this.capacitacionService = capacitacionService;
		this.contenidoWebService = contenidoWebService;
		this.contenidoArchivoService = contenidoArchivoService;
		this.contenidoImagenService = contenidoImagenService;
		this.usuarioService = usuarioService;
		this.tipoEstudioService = tipoEstudioService;
		this.resumenService = resumenService;
		this.beneficiarioService = beneficiarioService;
		this.parametroService = parametroService;
		this.enContenidoWebService = enContenidoWebService;
		this.enResumenService = enResumenService;
		this.mainRoute = mainRoute;
		this.context = context;
	}
    
    @GetMapping(value = "")
	public ModelAndView principalCapacitacion(Model  model) {
		model.addAttribute("lstBeneficarios", beneficiarioService.listAll());
		return new ModelAndView(ViewConstant.MAIN_CAPACITACION);
	}
    
	@GetMapping(value = "/nuevo")
	public ModelAndView formularioCapacitacion(Model model) {
		model.addAttribute("type", "capacitacion");
		model.addAttribute("lstBeneficiarios", beneficiarioService.listAll());
		model.addAttribute("lstTipoEstudios",tipoEstudioService.listAll());
		return new ModelAndView(ViewConstant.MAIN_CAPACITACION_NUEVO);
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody Capacitacion getCapacitacionById(@RequestParam(value = "id") int capacitacionId) {
		return capacitacionService.findOneById(capacitacionId);
	}
	
	@GetMapping(value = "/obtenerListado/{comodin}/{estado}/{beneficiario}")
	public @ResponseBody List<Capacitacion> listAllTrainings(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("beneficiario") String beneficiarioId) throws JsonProcessingException{
		
		List<Capacitacion> lstCapacitacion = new ArrayList<Capacitacion>();
		
		if(comodin.equals("0") && estado.equals("-1") && beneficiarioId.equals("0") ) {
			lstCapacitacion= capacitacionService.listAll();
		}else {
			if(comodin.equals("0") && beneficiarioId.equals("0")) {
					lstCapacitacion = capacitacionService.findAllByFlagActivo(Boolean.valueOf(estado));
			}else {
				List<Capacitacion> lstCapacitacionFilter = new ArrayList<>();
				comodin = comodin.equals("0") ? "":comodin;
				
				lstCapacitacion = capacitacionService.findAllByTituloPrincipalContainingIgnoreCase(comodin);
				if(!estado.equals("-1")) {
					for (Capacitacion x : lstCapacitacion) {
							
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
						for (Capacitacion x : lstCapacitacion) {
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
	public @ResponseBody String add(
			@ModelAttribute Capacitacion capacitacion,
			@RequestParam boolean flagResumen,
			@RequestParam(value="resumen", required = false) String txtResumen) {
		
			ContenidoWeb contenidoWeb = null;
			EnContenidoWeb eContenidoWeb = null;
			if(capacitacion.getId()> 0) {//Actualizacion
				Capacitacion qCapacitacion = capacitacionService.findOneById(capacitacion.getId());
				qCapacitacion.setTituloPrincipal(capacitacion.getTituloPrincipal());
				qCapacitacion.setTituloLargo(capacitacion.getTituloLargo());
				qCapacitacion.setDescripcion(capacitacion.getDescripcion());
				qCapacitacion.setFechaCapacitacion(capacitacion.getFechaCapacitacion());
				qCapacitacion.setTags(capacitacion.getTags());
				contenidoWeb = contenidoWebService.findOneById(qCapacitacion.getContenidoWeb().getId());
				contenidoWeb.setTitulo(qCapacitacion.getTituloPrincipal());
				contenidoWeb.setUrl(2, qCapacitacion.getTituloPrincipal().toLowerCase().replace(" ", "-"));
				contenidoWeb.setTags(capacitacion.getTags());
				contenidoWeb.addCapacitacion(qCapacitacion);
				//Resumen
				contenidoWeb.getResumen().setResumen(txtResumen);
				contenidoWeb.getResumen().setTitulo(contenidoWeb.getTitulo());
				contenidoWeb.getResumen().setUrl(contenidoWeb.getUrl());
				//Enviando a BD | Por transferencia de estados(Cascade) se registrará tanto el contenido como el capacitacion			
				contenidoWebService.add(contenidoWeb);
			}else {// R E G I S T R O
				EnCapacitacion eCapacitacion = null;
				
				capacitacion.setFlagActivo(true);
				capacitacion.setFechaCreacion();
				
				if(capacitacion.getFkEstudio() != null && capacitacion.getFkEstudio().length() > 0) {
					capacitacion.setEstudio(Integer.parseInt(capacitacion.getFkEstudio()));
					capacitacion.setBeneficiario(new Beneficiario(Integer.parseInt(capacitacion.getFkBeneficiario())));
				}
				
				contenidoWeb = new ContenidoWeb();
				//Agregando Contenido Web
				contenidoWeb.setTitulo(capacitacion.getTituloPrincipal());
				contenidoWeb.setTags(capacitacion.getTags());
				contenidoWeb.setUrl(2, capacitacion.getTituloPrincipal().toLowerCase().replace(" ", "-"));
				contenidoWeb.setFechaContenido(capacitacion.getFechaCapacitacion());
				contenidoWeb.setFechaCreacion();
				contenidoWeb.setFlagActivo(true);
				contenidoWeb.setFlagResumen(flagResumen);
				contenidoWeb.setUsuario(usuarioService.findIdByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
				contenidoWeb.setTipoContenido(2);
				
				//Copying
				eContenidoWeb =  new EnContenidoWeb();
				BeanUtils.copyProperties(contenidoWeb, eContenidoWeb);
				
				//Copying
				eCapacitacion = new EnCapacitacion();
				BeanUtils.copyProperties(capacitacion, eCapacitacion);
				if(capacitacion.getFkEstudio() != null && capacitacion.getFkEstudio().length() > 0)
					eCapacitacion.setEstudio(Integer.parseInt(capacitacion.getFkEstudio()));
				
				//Target
				contenidoWeb.addCapacitacion(capacitacion);
				eContenidoWeb.addEnCapacitacion(eCapacitacion);
				
				//Creando resumen
				if(flagResumen) {
					Resumen resumen = new Resumen();
					resumen.setResumen(txtResumen);
					
					//Copying
					EnResumen enResumen = new EnResumen();
					BeanUtils.copyProperties(resumen, enResumen);
					
					//Target
					eContenidoWeb.addResumen(enResumen);
					contenidoWeb.addResumen(resumen);
				}
				
				//Enviando a BD | Por transferencia de estados(Cascade) se registrará tanto el contenido como el capacitacion			
				contenidoWebService.add(contenidoWeb);
				//TB ENGLISH
				enContenidoWebService.add(eContenidoWeb);
			}
			
			return String.valueOf(contenidoWeb.getId());
	}
	
	@PutMapping(value = "/desactivar")
	public @ResponseBody String disabled(@RequestParam(value = "id") int id) {

			Capacitacion capacitacion = capacitacionService.findOneById(id);
			int cwId = capacitacion.getContenidoWeb().getId();
			boolean perteneceAInicio = false;
			if(capacitacion.isFlagActivo()){
				Integer[] capaIds =  (Integer[]) context.getAttribute("INDEX_PAGE_TRAININGS");
				for(int i=0;i<capaIds.length;i++){
					if(capaIds[i] == cwId){
						perteneceAInicio = true;
						break;
					}
				}
			}

			if(!perteneceAInicio){
				if(capacitacion.isFlagActivo()) {
					capacitacion.setFlagActivo(false);
				}else {
					capacitacion.setFlagActivo(true);
				}
				capacitacion.setFechaModificacion();
				capacitacionService.update(capacitacion);
				enCapacitacionService.updateStatusById(capacitacion.isFlagActivo(), id);
				//Actualizando el flag de la tabla contenido web
				contenidoWebService.updatingFlagActivoById(cwId, capacitacion.isFlagActivo());
				enContenidoWebService.updatingFlagActivoById(cwId, capacitacion.isFlagActivo());
				return "1";
			}else{
				return "-89";
			}
	}
	
	@GetMapping(value = "/obtenerArchivos")
	public @ResponseBody List<ContenidoArchivo> obtenerArchivos(@RequestParam(value = "capacitacionId") int estudioId) {
		return contenidoArchivoService.findAllByContenidoWebId(capacitacionService.findContenidoIdById(estudioId));
	}
	
	@GetMapping(value = "/obtenerImagenes")
	public @ResponseBody List<ContenidoImagen> obtenerImagenes(@RequestParam(value = "capacitacionId") int estudioId) {
		return contenidoImagenService.findAllByContenidoWebId(capacitacionService.findContenidoIdById(estudioId));
	}
	
	@RequestMapping(value = "/cargarArchivos", method = RequestMethod.POST)
	public @ResponseBody String guardarArchivo(
			@RequestPart(value = "fileImagenResumen", required = false) MultipartFile fileImagenResumen,
			@RequestParam(value = "Documentos", required = true) MultipartFile[] documentos,
			@RequestParam(value = "Imagenes", required = false) MultipartFile[] imagenes,
			@RequestParam(value = "ContenidoWebId") Integer contenidoWebId,
			@RequestParam(value = "Alias", required = true) String[] alias,
			@RequestParam(value = "SizeList", required = false) String[] sizes, HttpServletRequest request) throws JsonProcessingException {

		String rutaBase;
		if(alias.length == 0) {alias = new String[1];alias[0]="";}
		if (fileImagenResumen != null) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Capacitaciones/Imagenes/"+ contenidoWebId);
			guardarFile(fileImagenResumen, contenidoWebId, null, rutaBase, 0, alias, sizes);
		}
		if(documentos.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Capacitaciones/Archivos/"+ contenidoWebId);
			for (int i = 0; i < documentos.length; i++) {
				guardarFile(documentos[i], contenidoWebId, true, rutaBase, i, alias, sizes);
			}
		}
		
		if(imagenes.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Capacitaciones/Imagenes/"+ contenidoWebId);
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
				 * null: imagenPortada || Only for studies
				 */
				

				fullPath = rutaBase + "/"+ uuid + extension;
				
				File nuevoFile = new File(fullPath);
				if(typeFile == null) {
					Resumen qResumen = resumenService.findResumenById(contenidoWebId);
					// Actualizando el estudio
					qResumen.setRutaImagenPortada("/" + contenidoWebId + "/" + uuid + extension);
					qResumen.setNombreImagenPortada(file.getOriginalFilename());
					resumenService.update(qResumen);
					
					//TB INGLES
					EnResumen eResumen = new EnResumen();
					BeanUtils.copyProperties(qResumen, eResumen);
					eResumen.setContenidoWeb(contenidoWebId);
					enResumenService.update(eResumen);
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
			@RequestPart(value = "fileImagePortada", required = false) MultipartFile fileImagePortada,
			@RequestPart(value = "fileImagenResumen", required = false) MultipartFile fileImagenResumen,
			@RequestParam(value = "Documentos", required = false) MultipartFile[] documentos,
			@RequestParam(value = "Imagenes", required = false) MultipartFile[] imagenes,
			@RequestParam(value = "CapacitacionId") Integer capacitacionId,
			@RequestParam(value = "Alias", required = true) String[] alias,
			@RequestParam(value = "SizeList", required = false) String[] sizes, HttpServletRequest request) throws JsonProcessingException {

		String rutaBase;
		int contenidoWebId = capacitacionService.findContenidoIdById(capacitacionId);
		if(alias.length == 0) {alias = new String[1];alias[0]="";}
		
		if(documentos.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Capacitaciones/Archivos/"+ contenidoWebId);
			for (int i = 0; i < documentos.length; i++) {
				guardarFile(documentos[i], contenidoWebId, true, rutaBase, i, alias, sizes);
			}
		}
		
		if(imagenes.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Capacitaciones/Imagenes/"+ contenidoWebId);
			for (int i = 0; i < imagenes.length; i++) {
				guardarFile(imagenes[i], contenidoWebId, false, rutaBase, i, alias, sizes);
			}
		}

		return "1";
	}
	
	@PostMapping(value = "/imagen/resumen")
	public @ResponseBody String actualizar(
				@RequestPart(value = "file", required = false) MultipartFile file,
				@RequestParam(value = "id") int id){

		if(!file.isEmpty()) {
			
			Integer cwId = capacitacionService.findContenidoIdById(id);
			Utilitarios.createDirectory(mainRoute + "/Capacitaciones/Imagenes/"+ cwId);
			
			String[] splitNameFile = file.getOriginalFilename().split("\\.");
			String extension = "." + splitNameFile[splitNameFile.length - 1];
			String fullPath;
			
			UUID uuid = UUID.randomUUID(); 
			
			Resumen qResumen = resumenService.getResumenById(cwId);
			
			if(qResumen.getRutaImagenPortada() != null) {
				fullPath = mainRoute + "/" + "Capacitaciones/Imagenes/" + cwId + "/" + uuid + extension;
				qResumen.setRutaImagenPortada("/" + cwId + "/" + uuid + extension);
			}else {
				fullPath = mainRoute + "/" + "Capacitaciones/Imagenes/" + cwId + "/" + uuid + extension;
				qResumen.setRutaImagenPortada("/" + cwId + "/" + uuid + extension);
			}
			qResumen.setNombreImagenPortada(file.getOriginalFilename());
			resumenService.update(qResumen);
			enResumenService.updatingResumenImageById(qResumen.getNombreImagenPortada(), qResumen.getRutaImagenPortada(), cwId);
			// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
			// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
			File nuevoFile = new File(fullPath);
			try {
				file.transferTo(nuevoFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.info("> ROUTE: " + nuevoFile.getAbsolutePath());
			return "1";
		}else {
			LOGGER.info("> Imagen from request is null");
			return "-1";
		}
	}
	
	@DeleteMapping(value = "/eliminar/{id}")
	public @ResponseBody String eliminar(@PathVariable(value = "id") int id){
		//Obteniendo el cwId para eliminar en caso la capacitacion haya sido elegido como contenido de inicio
		Integer cwId = capacitacionService.findContenidoIdById(id);
		//Cascade DELETE
		capacitacionService.delete(id);
		enCapacitacionService.delete(id);
		if(cwId != null) {
			String paramName = "INDEX_PAGE_TRAININGS";
			Parametro qParametro = parametroService.findByClave(paramName);
			String idsInicio = qParametro.getValor();
			if(idsInicio.startsWith(cwId+",")){
				qParametro.setValor(idsInicio.replace(cwId+",",""));
				parametroService.update(qParametro);
				context.setAttribute(paramName, Parseador.stringArrayToIntArray(qParametro.getValor().split(",")));
			}
			
			if(idsInicio.contains(","+cwId+",")){
				qParametro.setValor(idsInicio.replace(","+cwId+",",","));
				parametroService.update(qParametro);
				context.setAttribute(paramName, Parseador.stringArrayToIntArray(qParametro.getValor().split(",")));
			}
			
			if(idsInicio.endsWith(","+cwId)) {
				qParametro.setValor(idsInicio.replace(","+cwId,""));
				parametroService.update(qParametro);
				context.setAttribute(paramName, Parseador.stringArrayToIntArray(qParametro.getValor().split(",")));
			}
			
			if(idsInicio.equals(String.valueOf(cwId))) {
				qParametro.setValor(idsInicio.replace(cwId+"","0"));
				parametroService.update(qParametro);
				context.setAttribute(paramName, Parseador.stringArrayToIntArray(qParametro.getValor().split(",")));
			}
		}
		return "1";
	}
	
}