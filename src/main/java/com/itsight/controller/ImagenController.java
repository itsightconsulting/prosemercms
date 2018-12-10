package com.itsight.controller;

import java.io.File;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.Utilitarios;
import com.itsight.domain.free.Imagen;
import com.itsight.service.ImagenService;

@RequestMapping("/gestion/imagen")
@Controller
public class ImagenController {
	
	private static final Logger LOGGER = LogManager.getLogger(ImagenController.class);
	
	private String mainRoute;
	
	private ImagenService imagenService;
	
	@Autowired
	public ImagenController(String mainRoute, ImagenService imagenService) {
		// TODO Auto-generated constructor stub
		this.mainRoute = mainRoute;
		this.imagenService = imagenService;
	}
	
	
	@RequestMapping(value = "/cargar", method = RequestMethod.POST)
	public @ResponseBody String guardarImagenCKeditor(
			@RequestPart(value = "file", required = true) MultipartFile file) throws JsonProcessingException {
		
		if (file != null) {
			String rutaBase = Utilitarios.createDirectory(mainRoute + "/Media/Imagenes");
			return guardarFile(rutaBase, file);
		}else {
			return "-9";
		}
	}
	
	private String guardarFile(String rutaBase, MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				
				String[] splitNameFile = file.getOriginalFilename().split("\\.");
				String extension = "." + splitNameFile[splitNameFile.length - 1];
				String fullPath = "";
				UUID uuid = UUID.randomUUID(); 
				
				Imagen imagen = new Imagen();
				imagen.setNombreMedia(file.getOriginalFilename());
				imagen.setUuid(uuid.toString());
				imagenService.add(imagen);
				//FULL ROUTE
				fullPath = rutaBase + "/" + uuid + extension;
				imagen.setRealMediaPath(fullPath);
				imagen.setRutaMediaWeb("/" + uuid + extension);
				imagenService.update(imagen);
				
				File nuevoFile = new File(fullPath);
				// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
				// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
				file.transferTo(nuevoFile);

				LOGGER.info("> ROUTE: " + fullPath);
				return imagen.getRutaMediaWeb();

			} catch (Exception e) {
				e.printStackTrace();
				return "-9";
			}
		} else {
			LOGGER.info("> Isn't a file");
			return "-1";
		}
	}

}
