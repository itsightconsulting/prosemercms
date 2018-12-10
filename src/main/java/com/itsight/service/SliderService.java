package com.itsight.service;

import java.util.List;

import com.itsight.domain.Slider;

public interface SliderService {

	List<Slider> listAll();
	
	Slider add(Slider slider);
	
	Slider update(Slider slider);
	
	void delete(int sliderId);
	
	Slider getSliderById(int sliderId);
			
	List<Slider> findAllByFlagActivo(boolean flagActivo);
	
	List<Slider> findAllByOrderByIdDesc();
	
	void updateSlidersPrincipal(int sliderId);


}
