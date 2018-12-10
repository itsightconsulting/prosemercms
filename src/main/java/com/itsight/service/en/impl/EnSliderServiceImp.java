package com.itsight.service.en.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnSlider;
import com.itsight.repository.en.EnSliderRepository;
import com.itsight.service.en.EnSliderService;

@Service
@Transactional
public class EnSliderServiceImp implements EnSliderService {
	
	private EnSliderRepository sliderRepository;
	
	@Autowired
	public EnSliderServiceImp(EnSliderRepository sliderRepository) {
		this.sliderRepository = sliderRepository;
	}

	@Override
	public List<EnSlider> listAll() {
		// TODO Auto-generated method stub
		return sliderRepository.findAll();
	}
	
	@Override
	public List<EnSlider> findAllByOrderByIdDesc(){
		return sliderRepository.findAllByOrderByIdDesc();
	}

	@Override
	public EnSlider add(EnSlider slider) {
		// TODO Auto-generated method stub
		return sliderRepository.save(slider);
	}

	@Override
	public EnSlider update(EnSlider slider) {
		// TODO Auto-generated method stub
		return sliderRepository.saveAndFlush(slider);
	}

	@Override
	public void delete(int sliderId) {
		// TODO Auto-generated method stub
		sliderRepository.delete(new Integer(sliderId));
	}

	@Override
	public EnSlider getSliderById(int sliderId) {
		// TODO Auto-generated method stub
		return sliderRepository.findOne(new Integer(sliderId));
	}

	@Override
	public List<EnSlider> findAllByFlagActivo(boolean flagActivo) {
		// TODO Auto-generated method stub
		return sliderRepository.findAllByFlagActivo(flagActivo);
	}

	@Override
	public void updateSlidersPrincipal(int sliderId) {
		// TODO Auto-generated method stub
		sliderRepository.updateSlidersPrincipal(sliderId);
	}
	
	

}
