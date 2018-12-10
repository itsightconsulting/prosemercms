package com.itsight.service.en.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnSliderImagen;
import com.itsight.repository.en.EnSliderImagenRepository;
import com.itsight.service.en.EnSliderImagenService;

@Service
@Transactional
public class EnSliderImagenServiceImpl implements EnSliderImagenService {

	
	private EnSliderImagenRepository sliderImagenRepository;
	
	@Autowired
	public EnSliderImagenServiceImpl(EnSliderImagenRepository sliderImagenRepository) {
		this.sliderImagenRepository = sliderImagenRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<EnSliderImagen> listAll() {
		// TODO Auto-generated method stub
		return sliderImagenRepository.findAll();
	}

	@Override
	public List<EnSliderImagen> findBySliderId(int sliderId) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.findBySliderId(sliderId);
	}

	@Override
	public EnSliderImagen getSliderImagenById(int sliderImagenId) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.findOne(new Integer(sliderImagenId));
	}

	@Override
	public EnSliderImagen add(EnSliderImagen sliderImagen) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.save(sliderImagen);
	}

	@Override
	public EnSliderImagen update(EnSliderImagen sliderImagen) {
		// TODO Auto-generated method stub
		return sliderImagenRepository.saveAndFlush(sliderImagen);
	}

	@Override
	public void delete(int sliderImagenId) {
		// TODO Auto-generated method stub
		sliderImagenRepository.delete(new Integer(sliderImagenId));
	}

}
