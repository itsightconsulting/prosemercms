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
import com.itsight.domain.free.Archivo;
import com.itsight.service.ArchivoService;

@RequestMapping("/gestion/archivo")
@Controller
public class ArchivoController {
	
	private static final Logger LOGGER = LogManager.getLogger(ArchivoController.class);
	
	private String mainRoute;
	
	private ArchivoService archivoService;
	
	@Autowired
	public ArchivoController(String mainRoute, ArchivoService archivoService) {
		// TODO Auto-generated constructor stub
		this.mainRoute = mainRoute;
		this.archivoService = archivoService;
	}
	
	
	@RequestMapping(value = "/cargar", method = RequestMethod.POST)
	public @ResponseBody String guardarArchivoCKeditor(
			@RequestPart(value = "file", required = true) MultipartFile file,
			@RequestPart(value = "alias", required = true) String alias,
			@RequestPart(value = "peso", required = true) String peso) throws JsonProcessingException {
		
		if (file != null) {
			String rutaBase = Utilitarios.createDirectory(mainRoute + "/Media/Archivos");
			return guardarFile(rutaBase, file,  alias, peso);
		}else {
			return "-9";
		}
	}
	
	private String guardarFile(String rutaBase, MultipartFile file, String alias, String peso) {
		if (!file.isEmpty()) {
			try {
				
				String[] splitNameFile = file.getOriginalFilename().split("\\.");
				String extension = "." + splitNameFile[splitNameFile.length - 1];
				String fullPath = "";
				UUID uuid = UUID.randomUUID(); 
				
				Archivo archivo = new Archivo();
				archivo.setAlias(alias.equals("") ? splitNameFile[0]:alias );
				archivo.setNombreMedia(alias.equals("") ? file.getOriginalFilename():alias+extension);
				archivo.setUuid(uuid.toString());
				archivo.setPeso(peso);
				archivoService.add(archivo);
				//FULL ROUTE
				fullPath = rutaBase + "/" + uuid + extension;
				archivo.setRealMediaPath(fullPath);
				archivo.setRutaMediaWeb("/" + archivo.getId() + "/" + uuid + extension);
				archivoService.update(archivo);
				
				File nuevoFile = new File(fullPath);
				// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
				// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
				file.transferTo(nuevoFile);

				LOGGER.info("> ROUTE: " + fullPath);
				return archivo.getRutaMediaWeb();

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
