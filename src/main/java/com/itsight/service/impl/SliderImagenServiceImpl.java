package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.SliderImagen;
import com.itsight.repository.SliderImagenRepository;
import com.itsight.service.SliderImagenService;

@Service
@Transactional
public class SliderImagenServiceImpl implements SliderImagenService {

	
	private SliderImagenRepository sliderImagenRepository;
	
	@Autowired
	public SliderImagenServiceImpl(SliderImagenRepository sliderImagenRepository) {
		this.sliderImagenRepository = sliderImagenRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<SliderImagen> listAll() {
		// TODO Auto-generated method stub
		return sliderImagenRepository.findAll();
	}

	@Override
	public List<SliderImagen> findBySliderId(int sliderId) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.findBySliderId(sliderId);
	}

	@Override
	public SliderImagen getSliderImagenById(int sliderImagenId) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.findOne(new Integer(sliderImagenId));
	}

	@Override
	public SliderImagen add(SliderImagen sliderImagen) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.save(sliderImagen);
	}

	@Override
	public SliderImagen update(SliderImagen sliderImagen) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.saveAndFlush(sliderImagen);
	}

	@Override
	public void delete(int sliderImagenId) {
		// TODO Auto-generated method stub
		sliderImagenRepository.delete(new Integer(sliderImagenId));
	}

}
