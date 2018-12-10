package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Slider;
import com.itsight.repository.SliderRepository;
import com.itsight.service.SliderService;

@Service
@Transactional
public class SliderServiceImp implements SliderService {
	
	private SliderRepository sliderRepository;
	
	@Autowired
	public SliderServiceImp(SliderRepository sliderRepository) {
		this.sliderRepository = sliderRepository;
	}

	@Override
	public List<Slider> listAll() {
		// TODO Auto-generated method stub
		return sliderRepository.findAll();
	}
	
	@Override
	public List<Slider> findAllByOrderByIdDesc(){
		return sliderRepository.findAllByOrderByIdDesc();
	}

	@Override
	public Slider add(Slider slider) {
		// TODO Auto-generated method stub
		return sliderRepository.save(slider);
	}

	@Override
	public Slider update(Slider slider) {
		// TODO Auto-generated method stub
		return sliderRepository.saveAndFlush(slider);
	}

	@Override
	public void delete(int sliderId) {
		// TODO Auto-generated method stub
		sliderRepository.delete(new Integer(sliderId));
	}

	@Override
	public Slider getSliderById(int sliderId) {
		// TODO Auto-generated method stub
		return sliderRepository.findOne(new Integer(sliderId));
	}

	@Override
	public List<Slider> findAllByFlagActivo(boolean flagActivo) {
		// TODO Auto-generated method stub
		return sliderRepository.findAllByFlagActivo(flagActivo);
	}

	@Override
	public void updateSlidersPrincipal(int sliderId) {
		// TODO Auto-generated method stub
		sliderRepository.updateSlidersPrincipal(sliderId);
	}
	
	

}
