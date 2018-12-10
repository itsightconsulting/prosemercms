package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.ContenidoWeb;
import com.itsight.domain.Memoria;
import com.itsight.domain.Resumen;
import com.itsight.service.ContenidoWebService;
import com.itsight.service.MemoriaService;
import com.itsight.service.ResumenService;
import com.itsight.service.en.EnResumenService;

@Controller
@RequestMapping("/gestion/memoria")
public class MemoriaController {
	
	private static final Logger LOGGER = LogManager.getLogger(MemoriaController.class);
			
	private MemoriaService memoriaService;
	
	private ContenidoWebService contenidoWebService;
	
	private ResumenService resumenService;
	
	private EnResumenService enResumenService;
	
	private String mainRoute;
						
	
    @Autowired
	public MemoriaController(MemoriaService memoriaService, ContenidoWebService contenidoWebService, ResumenService resumenService, EnResumenService enResumenService, String mainRoute) {
		this.memoriaService = memoriaService;
		this.contenidoWebService = contenidoWebService;
		this.resumenService = resumenService;
		this.enResumenService = enResumenService;
		this.mainRoute = mainRoute;
	}
    
    @GetMapping(value = "")
	public ModelAndView principalMemoria(Model  model) {
		return new ModelAndView(ViewConstant.MAIN_MEMORIA);
	}
	
	@PostMapping(value = "/guardar")
	public @ResponseBody String addMemoriaDelProyecto(
							@ModelAttribute Memoria memoria,
							@RequestParam(value="resumen", required = false) String txtResumen) {
		if(memoria.getId() == 1) {
			Memoria qMemoria = memoriaService.findOneById(1);
			qMemoria.setTituloPrincipal(memoria.getTituloPrincipal());
			qMemoria.setContenido(memoria.getContenido());
			qMemoria.setFechaModificacion();
			qMemoria.setModificadoPor(SecurityContextHolder.getContext().getAuthentication().getName());
			ContenidoWeb contenidoWeb = contenidoWebService.findOneById(2);//ID 2 Insertado mediante clase StartUpListener como memoria del proyecto(Id de CW relaconado a el registro de la tabla memoria
			//Resumen
			contenidoWeb.setTitulo(memoria.getTituloPrincipal());
			contenidoWeb.getResumen().setResumen(txtResumen);
			contenidoWeb.getResumen().setTitulo(memoria.getTituloPrincipal());
			memoriaService.add(qMemoria);
			return String.valueOf(memoria.getId());
		}else {
			return "-99";
		}
	}

	@GetMapping(value = "/obtener")
	public @ResponseBody Memoria getMemoriaById(@RequestParam(value = "id") int memoriaId) {
		return memoriaService.findOneById(memoriaId);
	}
	
	@GetMapping(value = "/obtenerListado")
	public @ResponseBody List<Memoria> listAllWhoAreUs() throws JsonProcessingException{
		return memoriaService.listAll();
	}
	
	@PutMapping(value = "/archivo")
	public @ResponseBody String actualizar(
				@RequestPart(value = "file", required = false) MultipartFile file,
				@RequestParam(value = "id") int id,
				@RequestParam(value = "peso") String peso,
				@RequestParam(value = "alias", required = false) String alias){

		if(!file.isEmpty()) {
			
			String[] splitNameFile = file.getOriginalFilename().split("\\.");
			String extension = "." + splitNameFile[splitNameFile.length - 1];
			String fullPath = "";
			
			UUID uuid = UUID.randomUUID(); 
			
			
			Memoria qMemoria = memoriaService.findOneById(id);
			
			if(qMemoria.getRutaArchivo() != null) {
				fullPath = mainRoute + "/" + "MemoriaProyecto" + "/" + uuid + extension;
				qMemoria.setRutaArchivo("/" + uuid + extension);
			}else {
				fullPath = mainRoute + "/" + "MemoriaProyecto" + "/" + uuid + extension;
				qMemoria.setRutaArchivo("/" + uuid + extension);
			}
			// Actualizando el archivo de memoria
			qMemoria.setPeso(peso);
				
				if(alias != null && alias.length()> 0) {
					qMemoria.setNombreArchivo(alias + extension);
				}else {
					qMemoria.setNombreArchivo(file.getOriginalFilename());
				}
				
			memoriaService.update(qMemoria);
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
	
	@PutMapping(value = "/imagen/resumen")
	public @ResponseBody String actualizarImagenResumen(
				@RequestPart(value = "file", required = false) MultipartFile file,
				@RequestParam(value = "id") int id){

		if(!file.isEmpty()) {
			
			String[] splitNameFile = file.getOriginalFilename().split("\\.");
			String extension = "." + splitNameFile[splitNameFile.length - 1];
			String fullPath = "";
			
			UUID uuid = UUID.randomUUID(); 
			
			Integer cwId = memoriaService.findContenidoIdById(id);
			Resumen qResumen = resumenService.getResumenById(cwId);
			
			if(qResumen.getRutaImagenPortada() != null) {
				fullPath = mainRoute + "/" + "MemoriaProyecto/" + uuid + extension;
				qResumen.setRutaImagenPortada("/" +uuid + extension);
			}else {
				fullPath = mainRoute + "/" + "MemoriaProyecto/" + uuid + extension;
				qResumen.setRutaImagenPortada("/" + uuid + extension);
			}
			qResumen.setNombreImagenPortada(file.getOriginalFilename());
				
			resumenService.updatingResumenImageById(qResumen.getNombreImagenPortada(), qResumen.getRutaImagenPortada(), cwId);
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
			
			String[] splitNameFile = file.getOriginalFilename().split("\\.");
			String extension = "." + splitNameFile[splitNameFile.length - 1];
			String fullPath = "";
			
			UUID uuid = UUID.randomUUID(); 
			
			Memoria qMemoria = memoriaService.findOneById(id);
			
			fullPath = mainRoute + "/" + "MemoriaProyecto/" + uuid + extension;
			qMemoria.setRutaImagenPortada("/" + uuid + extension);
			
			// Actualizando el estudio
			qMemoria.setNombreImagenPortada(file.getOriginalFilename());
			memoriaService.update(qMemoria);
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
}