package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnSliderImagen;

public interface EnSliderImagenService {

	List<EnSliderImagen> listAll();
	
	List<EnSliderImagen> findBySliderId(int sliderId);
	
	EnSliderImagen add(EnSliderImagen sliderImagen);
	
	EnSliderImagen update(EnSliderImagen sliderImagen);
	
	void delete(int sliderImagenId);
	
	EnSliderImagen getSliderImagenById(int sliderImagenId);
		
}
