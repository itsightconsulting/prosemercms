package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itsight.domain.ContenidoArchivo;
import com.itsight.service.ContenidoArchivoService;

@Controller
@RequestMapping(value = "gestion/contenido-archivo")
public class ContenidoArchivoController {

	public static final Logger LOGGER = LogManager.getLogger(ContenidoArchivoController.class);
	
	private ContenidoArchivoService contenidoArchivoService;
	
	@Autowired
	public ContenidoArchivoController(ContenidoArchivoService contenidoArchivoService) {
		// TODO Auto-generated constructor stub
		this.contenidoArchivoService = contenidoArchivoService;
	}
	
	@GetMapping(value = "/obtenerListado")
	public @ResponseBody List<ContenidoArchivo> listAll(@RequestParam int contenidoWebId){
		return contenidoArchivoService.findAllByContenidoWebId(contenidoWebId);
	}
	
	@DeleteMapping(value = "/eliminar/{id}")
	public @ResponseBody String eliminar(@PathVariable(value = "id") int id){
		contenidoArchivoService.delete(id);
		return "1";
	}
	
	@PutMapping(value = "/actualizar")
	public @ResponseBody String actualizar(
				@RequestPart(value = "file", required = false) MultipartFile file,
				@RequestParam(value = "id") int id,
				@RequestParam(value = "peso") String peso,
				@RequestParam(value = "alias", required = false) String alias){
		
		//Nuevo nombre file 
		String[] splitNameFile = file.getOriginalFilename().split("\\.");

		if(!file.isEmpty()) {
			
			ContenidoArchivo ci = contenidoArchivoService.getContenidoArchivoById(id);
			String pastRealMediaPath = ci.getRealMediaPath();
			String pastRutaMediaWeb = ci.getRutaMediaWeb();
			ci.setRealMediaPath(pastRealMediaPath.substring(0, pastRealMediaPath.indexOf("."))+"."+splitNameFile[1]);
			ci.setRutaMediaWeb(pastRutaMediaWeb.substring(0, pastRutaMediaWeb.indexOf("."))+"."+splitNameFile[1]);
			ci.setPeso(peso);
			if(alias != null && alias.length()> 0) {
				ci.setAlias(alias);
				ci.setNombreMedia(alias+"."+splitNameFile[1]);
			}else {
				ci.setAlias(splitNameFile[0]);
				ci.setNombreMedia(file.getOriginalFilename());
			}
				
			contenidoArchivoService.add(ci);
			// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
			// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
			File nuevoFile = new File(ci.getRealMediaPath());
			try {
				file.transferTo(nuevoFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.info("> ROUTE: " + ci.getRealMediaPath());
			return pastRutaMediaWeb.split("/")[1];
		}else {
			LOGGER.info("> Imagen from request is null");
			return "-1";
		}
	}
}
