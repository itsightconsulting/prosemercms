package com.itsight.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.itsight.constants.ViewConstant;
import com.itsight.domain.TipoImagen;
import com.itsight.service.TipoImagenService;

@Controller
@RequestMapping("/gestion/tipo-imagen")
public class TipoImagenController {
	
	private TipoImagenService tipoImagenService;
		
	@Autowired
	public TipoImagenController(TipoImagenService tipoImagenService) {
		// TODO Auto-generated constructor stub
		this.tipoImagenService = tipoImagenService;
	}
	
	@GetMapping(value = "")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModelAndView principalTipoImagen() {
		return new ModelAndView(ViewConstant.MAIN_TIPO_IMAGEN);
	}
	
	@GetMapping(value = "/listarTodos")
	public @ResponseBody List<TipoImagen> listAll() {
		return tipoImagenService.listAll();
	}
	
	@PostMapping(value = "/agregar")
	public @ResponseBody String addTipoImagen(@ModelAttribute TipoImagen tipoImagen) {
		if(tipoImagen.getId() == 0 ){

			tipoImagenService.add(tipoImagen);
			return "1";
		}else {
			tipoImagenService.update(tipoImagen);
			return "2";
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/actualizar-margenes")
	public @ResponseBody String actualizarMargenes(@RequestParam int id, @RequestParam Double ancho, @RequestParam Double alto) {
		if(ancho != null && ancho > 0 && ancho != null && ancho > 0) {
			TipoImagen ti = tipoImagenService.findOneById(id);
			ti.setAlto(BigDecimal.valueOf(alto));
			ti.setAncho(BigDecimal.valueOf(ancho));
			tipoImagenService.add(ti);
		} else {
			return "-9";
		}
		return "1";
	}
}
