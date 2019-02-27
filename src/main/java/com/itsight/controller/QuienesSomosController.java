package com.itsight.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.Utilitarios;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.ContenidoImagen;
import com.itsight.domain.QuienesSomos;
import com.itsight.service.ContenidoImagenService;
import com.itsight.service.QuienesSomosService;

@Controller
@RequestMapping("/gestion/quienes-somos")
public class QuienesSomosController {
	
	private static final Logger LOGGER = LogManager.getLogger(QuienesSomosController.class);
			
	private QuienesSomosService quienesSomosService;
	
	private ContenidoImagenService contenidoImagenService;
	
	private String mainRoute;
						
	
    @Autowired
	public QuienesSomosController(
						QuienesSomosService quienesSomosService, 
						ContenidoImagenService contenidoImagenService,
						String mainRoute) {
		this.quienesSomosService = quienesSomosService;
		this.contenidoImagenService = contenidoImagenService;
		this.mainRoute = mainRoute;
	}
    
    @GetMapping(value = "")
	public ModelAndView principalQuienesSomos(Model  model) {
		return new ModelAndView(ViewConstant.MAIN_QUIENES_SOMOS);
	}
	
	@PostMapping(value = "/guardar")
	public @ResponseBody String addWhoAreUs(
							@ModelAttribute QuienesSomos quienesSomos) {
		
		if(quienesSomos.getId()>0 && quienesSomos.getId()<6) {
			QuienesSomos qQuienesSomos = quienesSomosService.findOneById(quienesSomos.getId());
			qQuienesSomos.setContenido(quienesSomos.getContenido());
			qQuienesSomos.setFechaModificacion();
			qQuienesSomos.setModificadoPor(SecurityContextHolder.getContext().getAuthentication().getName());
			quienesSomosService.add(qQuienesSomos);
			return String.valueOf(quienesSomos.getId());
		}else {
			return "-99";
		}
	}

	@GetMapping(value = "/obtener")
	public @ResponseBody QuienesSomos getQuienesSomosById(@RequestParam(value = "id") int quienesSomosId) {
		return quienesSomosService.findOneById(quienesSomosId);
	}
	
	@GetMapping(value = "/obtenerListado")
	public @ResponseBody List<QuienesSomos> listAllWhoAreUs() throws JsonProcessingException{
		return quienesSomosService.listAll();
	}
	
	@GetMapping(value = "/obtenerImagenes")
	public @ResponseBody List<ContenidoImagen> obtenerImagenes(@RequestParam(value = "quienesSomosId") int quienesSomosId) {
		return contenidoImagenService.findAllByContenidoWebId(quienesSomosService.findContenidoIdById(quienesSomosId));
	}
	
	@RequestMapping(value = "/cargarArchivos", method = RequestMethod.POST)
	public @ResponseBody String guardarArchivosSecundary(
			@RequestParam(value = "Imagenes", required = false) MultipartFile[] imagenes,
			@RequestParam(value = "QuienesId") Integer quienesSomosId, HttpServletRequest request) throws JsonProcessingException {

		String rutaBase;
		int contenidoWebId = quienesSomosService.findContenidoIdById(quienesSomosId);
		
		if(imagenes.length > 0) {
			
			rutaBase = Utilitarios.createDirectory(mainRoute + "/Quienes/Historia/Imagenes/"+ contenidoWebId);
			for (int i = 0; i < imagenes.length; i++) {
				guardarFile(imagenes[i], contenidoWebId, rutaBase, i);
			}
		}
		return "1";
	}
	
	private void guardarFile(MultipartFile file, int contenidoWebId, String rutaBase, int i) {
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
				
				ContenidoImagen contenidoImagen = new ContenidoImagen();
				contenidoImagen.setNombreMedia(file.getOriginalFilename());
				contenidoImagen.setRealMediaPath(fullPath);
				contenidoImagen.setRutaMediaWeb("/" + contenidoWebId + "/" + uuid + extension);
				contenidoImagen.setUuid(uuid.toString());
				contenidoImagen.setContenidoWeb(contenidoWebId);
				contenidoImagen.setOrden(i);
				contenidoImagenService.add(contenidoImagen);
				
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
}
