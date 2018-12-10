package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnSlider;

public interface EnSliderService {

	List<EnSlider> listAll();
	
	EnSlider add(EnSlider slider);
	
	EnSlider update(EnSlider slider);
	
	void delete(int sliderId);
	
	EnSlider getSliderById(int sliderId);
			
	List<EnSlider> findAllByFlagActivo(boolean flagActivo);
	
	List<EnSlider> findAllByOrderByIdDesc();
	
	void updateSlidersPrincipal(int sliderId);


}
