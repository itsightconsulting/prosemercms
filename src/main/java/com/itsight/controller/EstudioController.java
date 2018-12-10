package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.itsight.domain.*;
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
import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.domain.en.EnEstudio;
import com.itsight.domain.en.EnResumen;
import com.itsight.service.BeneficiarioService;
import com.itsight.service.CapacitacionService;
import com.itsight.service.ContenidoArchivoService;
import com.itsight.service.ContenidoImagenService;
import com.itsight.service.ContenidoWebService;
import com.itsight.service.EstudioService;
import com.itsight.service.LogroService;
import com.itsight.service.ParametroService;
import com.itsight.service.ResumenService;
import com.itsight.service.TipoEstudioService;
import com.itsight.service.UsuarioService;
import com.itsight.service.en.EnCapacitacionService;
import com.itsight.service.en.EnContenidoWebService;
import com.itsight.service.en.EnEstudioService;
import com.itsight.service.en.EnLogroService;
import com.itsight.service.en.EnResumenService;

@Controller
@RequestMapping("/gestion/estudio")
public class EstudioController {
	
	private static final Logger LOGGER = LogManager.getLogger(EstudioController.class);
		
	private EstudioService estudioService;
	
	private BeneficiarioService beneficiarioService;
	
	private TipoEstudioService tipoEstudioService;
	
	private ContenidoWebService contenidoWebService;
	
	private ContenidoArchivoService contenidoArchivoService;
	
	private ContenidoImagenService contenidoImagenService;
	
	private UsuarioService usuarioService;
	
	private ResumenService resumenService;
	
	private LogroService logroService; 
	
	private CapacitacionService capacitacionService;
	
	private EnLogroService enLogroService; 
	
	private EnCapacitacionService enCapacitacionService;
	
	private ParametroService parametroService;
	
	private ServletContext context;

	
	//E N G L I S H  S E R V I C E S
	private EnContenidoWebService enContenidoWebService;
	private EnEstudioService enEstudioService;
	private EnResumenService enResumenService;
	
	private String mainRoute;
	
    @Autowired
	public EstudioController(
			BeneficiarioService beneficiarioService, 
			TipoEstudioService tipoEstudioService,
			EstudioService estudioService, 
			ContenidoWebService contenidoWebService, 
			ContenidoArchivoService contenidoArchivoService, 
			ContenidoImagenService contenidoImagenService, 
			UsuarioService usuarioService,
			ResumenService resumenService,
			EnContenidoWebService enContenidoWebService,
			EnEstudioService enEstudioService,
			EnResumenService enResumenService,
			LogroService logroService, 
			CapacitacionService capacitacionService,
			EnLogroService enLogroService, 
			EnCapacitacionService enCapacitacionService,
			ParametroService parametroService,
			ServletContext context,
			String mainRoute) {
		this.beneficiarioService = beneficiarioService;
		this.tipoEstudioService = tipoEstudioService;
		this.estudioService = estudioService;
		this.contenidoWebService = contenidoWebService;
		this.contenidoArchivoService = contenidoArchivoService;
		this.contenidoImagenService = contenidoImagenService;
		this.usuarioService = usuarioService;
		this.resumenService = resumenService;
		this.enContenidoWebService = enContenidoWebService;
		this.enEstudioService = enEstudioService;
		this.enResumenService = enResumenService;
		this.logroService = logroService;
		this.capacitacionService = capacitacionService;
		this.enLogroService = enLogroService;
		this.enCapacitacionService = enCapacitacionService;
		this.parametroService = parametroService;
		this.context  = context;
		this.mainRoute = mainRoute;
	}
    
    @GetMapping(value = "")
	public ModelAndView principalEstudio(Model  model) {
		model.addAttribute("lstTipoEstudios", tipoEstudioService.listAll());
		model.addAttribute("lstBeneficarios", beneficiarioService.listAll());
		return new ModelAndView(ViewConstant.MAIN_ESTUDIO);
	}
    
    @GetMapping(value = "/nuevo")
	public ModelAndView formularioEstudio(Model model) {
		model.addAttribute("lstBeneficarios", beneficiarioService.listAll());
		model.addAttribute("lstTipoEstudios", tipoEstudioService.listAll());
		model.addAttribute("type", "estudio");
		return new ModelAndView(ViewConstant.MAIN_ESTUDIO_NUEVO);
	}
    
    @PostMapping(value = "/agregar")
	public @ResponseBody String addResearch(
				@ModelAttribute Estudio estudio, 
				@RequestParam boolean flagResumen,
				@RequestParam(value="resumen", required = false) String txtResumen) {
		ContenidoWeb contenidoWeb;
		EnContenidoWeb enContenidoWeb;

			if(estudio.getId()> 0) {
				Estudio qEstudio;
				qEstudio = estudioService.findOneById(estudio.getId());
				qEstudio.setTituloPrincipal(estudio.getTituloPrincipal());
				qEstudio.setTituloLargo(estudio.getTituloLargo());
				qEstudio.setAlcance(estudio.getAlcance());
				qEstudio.setTags(estudio.getTags());
				qEstudio.setFechaEstudio(estudio.getFechaEstudio());
				contenidoWeb = contenidoWebService.findOneById(qEstudio.getContenidoWeb().getId());
				contenidoWeb.setTitulo(qEstudio.getTituloPrincipal());
				contenidoWeb.setUrl(1, qEstudio.getTituloPrincipal().toLowerCase().replace(" ", "-"));
				contenidoWeb.setTags(estudio.getTags());
				contenidoWeb.addEstudio(qEstudio);
				//Resumen
				contenidoWeb.getResumen().setResumen(txtResumen);
				contenidoWeb.getResumen().setTitulo(contenidoWeb.getTitulo());
				contenidoWeb.getResumen().setUrl(contenidoWeb.getUrl());
				contenidoWebService.add(contenidoWeb);

			}else {
				contenidoWeb = new ContenidoWeb();

				//Estudio
				estudio.setFlagActivo(true);
				estudio.setFechaCreacion();
				estudio.setBeneficiario(estudio.getFkBeneficiario());
				estudio.setTipoEstudio(estudio.getFkTipoEstudio());
				

				//Agregando Contenido Web
				contenidoWeb.setTitulo(estudio.getTituloPrincipal());
				contenidoWeb.setTags(estudio.getTags());
				contenidoWeb.setUrl(1, estudio.getTituloPrincipal().toLowerCase().replace(" ", "-"));
				contenidoWeb.setFechaCreacion();
				contenidoWeb.setFechaContenido(estudio.getFechaEstudio());
				contenidoWeb.setFlagActivo(true);
				contenidoWeb.setFlagResumen(flagResumen);
				contenidoWeb.setUsuario(usuarioService.findIdByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
				contenidoWeb.setTipoContenido(1);
				
				//Copying
				enContenidoWeb =  new EnContenidoWeb();
				BeanUtils.copyProperties(contenidoWeb, enContenidoWeb);
				
				//Copying
				EnEstudio enEstudio = new EnEstudio();
				enEstudio.setBeneficiario(estudio.getFkBeneficiario());
				BeanUtils.copyProperties(estudio, enEstudio);
				
				//Target
				enContenidoWeb.addEnEstudio(enEstudio);
				contenidoWeb.addEstudio(estudio);
				
				//Creando resumen
				if(flagResumen) {
					Resumen resumen = new Resumen();
					resumen.setResumen(txtResumen);
					//Copying
					EnResumen enResumen = new EnResumen();
					BeanUtils.copyProperties(resumen, enResumen);
					
					//Target
					enContenidoWeb.addResumen(enResumen);
					contenidoWeb.addResumen(resumen);
				}
				
				//Enviando a BD | Por transferencia de estados(Cascade) se registrará tanto el contenido como el estudio
				contenidoWebService.add(contenidoWeb);
				//Registro a tablas en ingles
				enContenidoWebService.add(enContenidoWeb);
			}
			
			return String.valueOf(contenidoWeb.getId());
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody Estudio getEstudioById(@RequestParam(value = "id") int estudioId) {
		return estudioService.findOneWithTipoEstudioById(estudioId);
	}
    
    @GetMapping(value = "/obtenerListado/{comodin}/{estado}/{beneficiario}")
	public @ResponseBody List<Estudio> listAllResearchs(
										@PathVariable("comodin") String comodin,
										@PathVariable("estado") String estado,
										@PathVariable("beneficiario") String beneficiarioId){
		
		List<Estudio> lstEstudio;
		
		if(comodin.equals("0") && estado.equals("-1") && beneficiarioId.equals("0") ) {
			lstEstudio= estudioService.listAll();
		}else {
			if(comodin.equals("0") && beneficiarioId.equals("0")) {
					lstEstudio = estudioService.findAllByFlagActivo(Boolean.valueOf(estado));
			}else {
				List<Estudio> lstEstudioFilter = new ArrayList<>();
				comodin = comodin.equals("0") ? "":comodin;
				
				lstEstudio = estudioService.findAllByTituloPrincipalContainingIgnoreCase(comodin);
				if(!estado.equals("-1")) {
					for (Estudio x : lstEstudio) {
							
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
						for (Estudio x : lstEstudio) {
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
	public @ResponseBody List<Estudio> list(
										@PathVariable("tipo") String tipo,
										@PathVariable("beneficiario") String beneficiario,
										@PathVariable("comodin") String comodin) throws JsonProcessingException{
		
		List<Estudio> lstEstudio = new ArrayList<Estudio>();
		if(!comodin.equals("0")) {
			
			lstEstudio = estudioService.listTopByTituloPrincipalAndBeneficiarioAndTipo(comodin, Integer.parseInt(beneficiario), Integer.parseInt(tipo));
		}else {
			lstEstudio = estudioService.listTopByTipoEstudioIdAndBeneficiario(Integer.parseInt(tipo), Integer.parseInt(beneficiario));
		}
		
		return lstEstudio;
	}
	
	@PutMapping(value = "/desactivar")
	public @ResponseBody String disabled(@RequestParam(value = "id") int id) {
		Estudio estudio = estudioService.findOneById(id);
		int cwId = estudio.getContenidoWeb().getId();

		boolean perteneceAInicio = false;
		String tipoEstudioIds = estudio.getTipoEstudio().getId() == 1 ? "INDEX_PAGE_STUDIES_1" : "INDEX_PAGE_STUDIES_2";
		if (estudio.isFlagActivo()) {
			Integer[] estIds = (Integer[]) context.getAttribute(tipoEstudioIds);
			for (int i = 0; i < estIds.length; i++) {
				if (estIds[i] == cwId) {
					perteneceAInicio = true;
					break;
				}
			}
		}

		if (!perteneceAInicio) {
			if (estudio.isFlagActivo()) {
				estudio.setFlagActivo(false);
			} else {
				estudio.setFlagActivo(true);
			}
			estudio.setFechaModificacion();
			estudioService.update(estudio);
			enEstudioService.updateStatusById(estudio.isFlagActivo(), id);
			//Actualizando el flag de la tabla contenido web
			contenidoWebService.updatingFlagActivoById(cwId, estudio.isFlagActivo());
			enContenidoWebService.updatingFlagActivoById(cwId, estudio.isFlagActivo());
			return "1";
		}else{
			return "-89";
		}
	}
	
	@GetMapping(value = "/obtenerArchivos")
	public @ResponseBody List<ContenidoArchivo> obtenerArchivos(@RequestParam(value = "estudioId") int estudioId) {
		return contenidoArchivoService.findAllByContenidoWebId(estudioService.findContenidoIdById(estudioId));
	}
	
	@GetMapping(value = "/obtenerImagenes")
	public @ResponseBody List<ContenidoImagen> obtenerImagenes(@RequestParam(value = "estudioId") int estudioId) {
		return contenidoImagenService.findAllByContenidoWebId(estudioService.findContenidoIdById(estudioId));
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
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Estudios/Imagenes/"+ contenidoWebId);
			guardarFile(fileImagePortada, contenidoWebId, null, rutaBase, 0, alias, sizes);
		}
		
		if (fileImagenResumen != null) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Estudios/Imagenes/"+ contenidoWebId);
			guardarFile(fileImagenResumen, contenidoWebId, null, rutaBase, -1, alias, sizes);
		}
		
		if(documentos.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Estudios/Archivos/"+ contenidoWebId);
			for (int i = 0; i < documentos.length; i++) {
				guardarFile(documentos[i], contenidoWebId, true, rutaBase, i, alias, sizes);
			}
		}
		
		if(imagenes.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Estudios/Imagenes/"+ contenidoWebId);
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
						// Actualizando el resumen
						qResumen.setRutaImagenPortada("/" + contenidoWebId + "/" + uuid + extension);
						qResumen.setNombreImagenPortada(file.getOriginalFilename());
						resumenService.update(qResumen);
						//TB INGLES
						EnResumen eResumen = new EnResumen();
						BeanUtils.copyProperties(qResumen, eResumen);
						eResumen.setContenidoWeb(contenidoWebId);
						enResumenService.update(eResumen);
						
					}else {
						Estudio qEstudio = estudioService.getEstudioByContenidoWebId(contenidoWebId);
						// Actualizando el estudio
						qEstudio.setRutaImagenPortada("/" + contenidoWebId + "/" + uuid + extension);
						qEstudio.setNombreImagenPortada(file.getOriginalFilename());
						estudioService.update(qEstudio);

						//TB INGLES
						EnEstudio eEstudio = new EnEstudio();
						BeanUtils.copyProperties(qEstudio, eEstudio);
						eEstudio.setContenidoWeb(contenidoWebId);
						eEstudio.setBeneficiario(qEstudio.getBeneficiario().getId());
						enEstudioService.update(eEstudio);
						
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
			@RequestPart(value = "fileImagePortada", required = false) MultipartFile fileImagePortada,
			@RequestPart(value = "fileImagenResumen", required = false) MultipartFile fileImagenResumen,
			@RequestParam(value = "Documentos", required = false) MultipartFile[] documentos,
			@RequestParam(value = "Imagenes", required = false) MultipartFile[] imagenes,
			@RequestParam(value = "EstudioId") Integer estudioId,
			@RequestParam(value = "Alias", required = true) String[] alias,
			@RequestParam(value = "SizeList", required = false) String[] sizes, HttpServletRequest request) throws JsonProcessingException {

		String rutaBase;
		int contenidoWebId = estudioService.findContenidoIdById(estudioId);
		if(alias.length == 0) {alias = new String[1];alias[0]="";}
		
		if(documentos.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Estudios/Archivos/"+ contenidoWebId);
			for (int i = 0; i < documentos.length; i++) {
				guardarFile(documentos[i], contenidoWebId, true, rutaBase, i, alias, sizes);
			}
		}
		
		if(imagenes.length > 0) {
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Estudios/Imagenes/"+ contenidoWebId);
			for (int i = 0; i < imagenes.length; i++) {
				guardarFile(imagenes[i], contenidoWebId, false, rutaBase, i, alias, sizes);
			}
		}

		return "1";

	}
	
	@PutMapping(value = "/imagen/resumen")
	public @ResponseBody String actualizarImagenResumen(
				@RequestPart(value = "file", required = false) MultipartFile file,
				@RequestParam(value = "id") int id){

		if(!file.isEmpty()) {
			
			Integer cwId = estudioService.findContenidoIdById(id);
			Utilitarios.createDirectory(mainRoute + "/Estudios/Imagenes/"+ cwId);

			
			String[] splitNameFile = file.getOriginalFilename().split("\\.");
			String extension = "." + splitNameFile[splitNameFile.length - 1];
			String fullPath = "";
			
			UUID uuid = UUID.randomUUID(); 			
			Resumen qResumen = resumenService.getResumenById(cwId);
			
			if(qResumen.getRutaImagenPortada() != null) {
				fullPath = mainRoute + "/" + "Estudios/Imagenes" + "/" + cwId + "/" + uuid + extension;
				qResumen.setRutaImagenPortada("/" + cwId + "/" + uuid + extension);
			}else {
				fullPath = mainRoute + "/" + "Estudios/Imagenes/" + cwId + "/" + uuid + extension;
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
	
	@PutMapping(value = "/imagen/portada")
	public @ResponseBody String actualizarPortada(
				@RequestPart(value = "file", required = false) MultipartFile file,
				@RequestParam(value = "id") int id){

		if(!file.isEmpty()) {
			
			Estudio qEstudio = estudioService.findOneById(id);
			Integer cwId = qEstudio.getContenidoWeb().getId();
			Utilitarios.createDirectory(mainRoute + "/Estudios/Imagenes/"+ cwId);

			String[] splitNameFile = file.getOriginalFilename().split("\\.");
			String extension = "." + splitNameFile[splitNameFile.length - 1];
			String fullPath = "";
			
			UUID uuid = UUID.randomUUID(); 
						
			if(qEstudio.getRutaImagenPortada() != null) {
				fullPath = mainRoute + "/" + "Estudios/Imagenes/" + cwId + "/" + uuid + extension;
				qEstudio.setRutaImagenPortada("/" +cwId  +"/" + uuid  + extension);
			}else {
				fullPath = mainRoute + "/" + "Estudios/Imagenes/" + cwId + "/" + uuid + extension;
				qEstudio.setRutaImagenPortada("/" +cwId  +"/" + uuid + extension);
			}
			
			// Actualizando el estudio
			qEstudio.setNombreImagenPortada(file.getOriginalFilename());
			estudioService.update(qEstudio);
			enEstudioService.updatingPortadaImageById(qEstudio.getNombreImagenPortada(), qEstudio.getRutaImagenPortada(), id);
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
		Integer cwId = estudioService.findContenidoIdById(id);
		Integer tipoEstudioId =  estudioService.findTipoEstudioIdById(id);
		//Cascade DELETE
		estudioService.delete(id);
		enEstudioService.delete(id);
		if(cwId != null) {
			String paramName = "";
			if(tipoEstudioId == 1) {
				paramName = "INDEX_PAGE_STUDIES_1";
			}else {
				paramName = "INDEX_PAGE_STUDIES_2";
			}
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
	
	@GetMapping(value = "/verificar/relacion/{id}")
	public @ResponseBody String verificarRelaciones(@PathVariable(value = "id") int id){
		//Obteniendo el cwId para eliminar en caso la capacitacion haya sido elegido como contenido de inicio
		boolean reLogro = false, reCapacitacion = false;
		List<Integer> lstCapacitacion =  capacitacionService.findIdsByEstudioId(id);
		List<Integer> lstLogro = logroService.findIdsByEstudioId(id);
		
		if(lstCapacitacion != null && lstCapacitacion.size() > 0)
			reCapacitacion = true;
		if(lstLogro != null && lstLogro.size() > 0)
			reLogro = true;
		
		if(reCapacitacion && !reLogro) {
			return "1";
		}else if(reLogro && !reCapacitacion) {
			return "2";
		}else if(reLogro && reCapacitacion) {
			return "3";
		}else {
			return "0";
		}
	}
	
	@PutMapping(value = "/relacion/deshacer/{id}/{strategy}")
	public @ResponseBody String verificarRelaciones(@PathVariable(value = "id") int id, @PathVariable(value = "strategy") int strategy){
		//Obteniendo el cwId para eliminar en caso la capacitacion haya sido elegido como contenido de inicio
		switch (strategy) {
		case 1:
			capacitacionService.updateTakeOffRelacionEstudio(id);
			enCapacitacionService.updateTakeOffRelacionEstudio(id);
			break;
		case 2:
			logroService.updateTakeOffRelacionEstudio(id);
			enLogroService.updateTakeOffRelacionEstudio(id);

			break;
		case 3:
			capacitacionService.updateTakeOffRelacionEstudio(id);
			enCapacitacionService.updateTakeOffRelacionEstudio(id);
			logroService.updateTakeOffRelacionEstudio(id);
			enLogroService.updateTakeOffRelacionEstudio(id);
			break;
		default:
			return "-9";
		}
		return "1";
	}
	
}