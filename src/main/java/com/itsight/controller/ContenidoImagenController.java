package com.itsight.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

import com.itsight.domain.ContenidoImagen;
import com.itsight.service.ContenidoImagenService;

@Controller
@RequestMapping(value = "gestion/contenido-imagen")
public class ContenidoImagenController {

	public static final Logger LOGGER = LogManager.getLogger(ContenidoImagenController.class);
	
	private ContenidoImagenService contenidoImagenService;
	
	@Autowired
	public ContenidoImagenController(ContenidoImagenService contenidoImagenService) {
		// TODO Auto-generated constructor stub
		this.contenidoImagenService = contenidoImagenService;
	}
	
	@GetMapping(value = "/obtenerListado")
	public @ResponseBody List<ContenidoImagen> listAll(@RequestParam int contenidoWebId){
		return contenidoImagenService.findAllByContenidoWebId(contenidoWebId);
	}
	
	@DeleteMapping(value = "/eliminar/{id}")
	public @ResponseBody String eliminar(@PathVariable(value = "id") int id){
		contenidoImagenService.delete(id);
		return "1";
	}
	
	@PutMapping(value = "/actualizar")
	public @ResponseBody String actualizar(
				@RequestPart(value = "image", required = false) MultipartFile image,
				@RequestParam(value = "id") int id){
		
		//Nuevo nombre file 
		String[] splitNameFile = image.getOriginalFilename().split("\\.");

		if(!image.isEmpty()) {
			UUID uuid = UUID.randomUUID();
			ContenidoImagen ci = contenidoImagenService.getContenidoImagenById(id);
			String pastRealMediaPath = ci.getRealMediaPath();
			String pastRutaMediaWeb = ci.getRutaMediaWeb();
			ci.setRealMediaPath(pastRealMediaPath.substring(0, pastRealMediaPath.lastIndexOf("/")+1)+uuid+"."+splitNameFile[1]);
			ci.setRutaMediaWeb(pastRutaMediaWeb.substring(0, pastRutaMediaWeb.lastIndexOf("/")+1)+uuid+"."+splitNameFile[1]);
			ci.setNombreMedia(image.getOriginalFilename());
			contenidoImagenService.add(ci);
			// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
			// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
			File nuevoFile = new File(ci.getRealMediaPath());
			try {
				image.transferTo(nuevoFile);
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
