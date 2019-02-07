package com.itsight.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itsight.constants.ViewConstant;
import com.itsight.domain.Slider;
import com.itsight.domain.en.EnSlider;
import com.itsight.service.SliderService;
import com.itsight.service.en.EnSliderService;

@Controller
@RequestMapping("/gestion/slider")
public class SliderController {

	private static final Logger LOGGER = LogManager.getLogger(SliderController.class);
	
	private String mainRoute;
	
	private SliderService sliderService;
	
	private EnSliderService enSliderService;
		
	@Autowired
	public SliderController(SliderService sliderService, EnSliderService enSliderService, String mainRoute) {
		// TODO Auto-generated constructor stub
		this.sliderService = sliderService;
		this.enSliderService = enSliderService;
		this.mainRoute = mainRoute;
	}
	
	@GetMapping(value = "/inicio")
	public ModelAndView principalSlider() {
		return new ModelAndView(ViewConstant.MAIN_SLIDER);
	}
	
	@GetMapping(value = "/listarTodos")
	public @ResponseBody List<Slider> listAllSliderWithoutFilters() throws JsonProcessingException {
			return sliderService.findAllByOrderByIdDesc();	
	}
	
	@GetMapping(value = "/obtenerListado/{estado}")
	public @ResponseBody List<Slider> listAllSlider(
										@PathVariable("estado") String estado) throws JsonProcessingException{
		List<Slider>
		 lstSlider = new ArrayList<Slider>();
		
		if(estado.equals("-1")) {
			lstSlider = sliderService.findAllByOrderByIdDesc();	
		}else {
			lstSlider = sliderService.findAllByFlagActivo(Boolean.valueOf(estado));
		}
		
		return lstSlider;
	}
	
	@GetMapping(value = "/obtener")
	public @ResponseBody Slider getSliderById(@RequestParam(value = "id") int sliderId) {
		return sliderService.getSliderById(sliderId);
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addSlider(@ModelAttribute Slider slider) {
			
			if(slider.getId() == 0 ){// R E G I S T R A R 
				slider.setRutaImagenWeb("none");
				slider.setRutaImagenMobile("none");
				if(slider.getHyperVinculo().equals("")) {
					slider.setHyperVinculo("javascript:void(0);");
				}
				//Copyng
				EnSlider eSlider = new EnSlider();
				BeanUtils.copyProperties(slider, eSlider);
				sliderService.add(slider);
				enSliderService.add(eSlider);
				if(slider.getFlagPrincipal())
					sliderService.updateSlidersPrincipal(slider.getId());
					enSliderService.updateSlidersPrincipal(eSlider.getId());
				return "1";
			}else {
				sliderService.update(slider);
				if(slider.getFlagPrincipal())
					sliderService.updateSlidersPrincipal(slider.getId());
				return "2";
			}
	}
	
	@PostMapping(value = "/desactivar")
	public @ResponseBody String disabledSlider(@RequestParam(value = "id") int sliderId) {
			Slider slider = sliderService.getSliderById(sliderId);

			if(slider.isFlagActivo()) {
				slider.setFlagActivo(false);
			}else {
				slider.setFlagActivo(true);
			}
			sliderService.update(slider);
			return "1";
	}
	
	@GetMapping(value = "/principal")
	public @ResponseBody String doPrincipal(@RequestParam(value = "id") int sliderId) {

			sliderService.updateSlidersPrincipal(sliderId);
			Slider qSlider = sliderService.getSliderById(sliderId);
			qSlider.setFlagPrincipal(true);
			sliderService.update(qSlider);
			return "1";
	}
		
	@RequestMapping(value = "/cargarImagen", method = RequestMethod.POST)
	public @ResponseBody String registrarFileVigenciaPoder(
			@RequestParam(value = "imagen", required = true) MultipartFile imagen,
			@RequestParam(value = "sliderId", required = true) Integer sliderId, 
			@RequestParam(value = "tipoImagenId", required = true) Integer tipoImagenId, HttpServletRequest request) {
		
		if (imagen != null) {

			switch(tipoImagenId) {
			case 1:
				guardarFile(imagen, sliderId, "ABC");
			case 2:
				guardarFile(imagen, sliderId, "DEF");
			default:
				return "-1";
			}
		}

		return "1";

	}
	
	private void guardarFile(MultipartFile file, Integer sliderId, String tipoImagen) {
		if (!file.isEmpty()) {
			try {

				String[] splitNameFile = file.getOriginalFilename().split("\\.");
				String extension = "." + splitNameFile[splitNameFile.length - 1];
				String fullPath = "";

				fullPath = mainRoute + "/Slideres/Slideres_" + tipoImagen +"_" + sliderId + extension;
				File nuevoFile = new File(fullPath);

				// Agregando la ruta a la base de datos
				
				Slider qSlider = sliderService.getSliderById(sliderId);
				sliderService.update(qSlider);

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
	
	@GetMapping(value = "/eliminar/{id}")
	public @ResponseBody String realizarBaja(@PathVariable(value = "id") int sliderId) {
			try {
				sliderService.delete(sliderId);

			} catch (Exception e) {
				// TODO: handle exception
				return "-9";
			}
			return "1";
	}
}
