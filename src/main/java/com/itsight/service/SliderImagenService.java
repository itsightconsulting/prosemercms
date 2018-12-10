package com.itsight.service;

import java.util.List;

import com.itsight.domain.SliderImagen;

public interface SliderImagenService {

	List<SliderImagen> listAll();
	
	List<SliderImagen> findBySliderId(int sliderId);
	
	SliderImagen add(SliderImagen sliderImagen);
	
	SliderImagen update(SliderImagen sliderImagen);
	
	void delete(int sliderImagenId);
	
	SliderImagen getSliderImagenById(int sliderImagenId);
		
}
