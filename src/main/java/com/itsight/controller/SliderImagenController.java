package com.itsight.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.domain.Slider;
import com.itsight.domain.SliderImagen;
import com.itsight.service.SliderImagenService;
import com.itsight.service.SliderService;

@Controller
@RequestMapping("/gestion/slider-imagen")
public class SliderImagenController {
	
	private static final Logger LOGGER = LogManager.getLogger(SliderImagenController.class);

	private SliderImagenService sliderImagenService;
	
	private SliderService sliderService;
	
	private String mainRoute;
		
	@Autowired
	public SliderImagenController(SliderImagenService sliderImagenService, SliderService sliderService, String mainRoute) {
		// TODO Auto-generated constructor stub
		this.sliderImagenService = sliderImagenService;
		this.sliderService = sliderService;
		this.mainRoute = mainRoute;
	}
	
	@GetMapping(value = "/listarTodos")
	public @ResponseBody List<SliderImagen> listAll(@RequestParam(value = "id") int sliderId) throws JsonProcessingException {
		return sliderImagenService.findBySliderId(sliderId);
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addSliderImagen(@ModelAttribute SliderImagen sliderImagen) {
			
			if(sliderImagen.getId() == 0 ){
				
				sliderImagenService.add(sliderImagen);
				return "1";
			}else {
				sliderImagenService.update(sliderImagen);
				return "2";
			}
	}
	
	@RequestMapping(value = "/cargarImagen", method = RequestMethod.POST)
	public @ResponseBody String guardarArchivo(
			@RequestPart(value = "fileSliderImagen", required = true) MultipartFile fileSliderImagen,
			@RequestParam(value = "sliderId", required = true) Integer sliderId,
			@RequestParam(value = "tipoImagenId", required = true) Integer tipoImagenId, HttpServletRequest request) {

		if (fileSliderImagen != null) {
			guardarFile(fileSliderImagen, sliderId, tipoImagenId );
		}

		return "1";

	}
	
	private void guardarFile(MultipartFile file, int sliderId, int tipoImagenId) {
		if (!file.isEmpty()) {
			try {

				String[] splitNameFile = file.getOriginalFilename().split("\\.");
				String extension = "." + splitNameFile[splitNameFile.length - 1];
				String fullPath = "";
				UUID uuid = UUID.randomUUID();

				fullPath = mainRoute + "/Slider/" + uuid + extension;
				File nuevoFile = new File(fullPath);

				// Agregando la ruta a la base de datos
				
				SliderImagen qSliderImagen = new SliderImagen();
				qSliderImagen.setSlider(sliderId);
				qSliderImagen.setTipoImagen(tipoImagenId);
				qSliderImagen.setRutaMedia(fullPath);
				qSliderImagen.setNombreMedia(nuevoFile.getName());
				
				Slider qSlider = sliderService.getSliderById(sliderId);
				if(tipoImagenId == 1) {
					qSlider.setRutaImagenWeb(""+uuid + extension);
				}else if(tipoImagenId == 2) {
					qSlider.setRutaImagenMobile(""+uuid + extension);
				}
				
				// Pasando la imagen de la vista a un arreglo de bytes para luego convertirlo y
				// transferirlo a un nuevo file con un nombre, ruta generado con anterioridad
				file.transferTo(nuevoFile);
				sliderService.update(qSlider);
				sliderImagenService.add(qSliderImagen);
				
				LOGGER.info("> ROUTE: " + fullPath);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("> Isn't a file");
		}
	}
}
