package com.itsight.service.en.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnResumen;
import com.itsight.repository.en.EnResumenRepository;
import com.itsight.service.en.EnResumenService;

@Service
@Transactional
public class EnResumenServiceImpl implements EnResumenService {

	
	private EnResumenRepository resumenRepository;
	
	@Autowired
	EnResumenServiceImpl(EnResumenRepository resumenRepository){
		this.resumenRepository = resumenRepository;
	}
	
	@Override
	public List<EnResumen> listAll() {
		// TODO Auto-generated method stub
		return resumenRepository.findAll();
	}

	@Override
	public EnResumen getResumenById(int ResumenId) {
		// TODO Auto-generated method stub
		return resumenRepository.findOne(new Integer(ResumenId));
	}
	@Override
	public List<EnResumen> findAllByIdIn(Integer[] ids) {
		// TODO Auto-generated method stub
		return resumenRepository.findAllByIdIn(ids);
	}

	@Override
	public EnResumen findResumenById(Integer contenidoWebId) {
		// TODO Auto-generated method stub
		return resumenRepository.findResumenById(contenidoWebId);
	}
	

	@Override
	public EnResumen update(EnResumen resumen) {
		// TODO Auto-generated method stub
		return resumenRepository.saveAndFlush(resumen);
	}

	@Override
	public void delete(int contenidoWebId) {
		// TODO Auto-generated method stub
		resumenRepository.delete(new Integer(contenidoWebId));
	}

	@Override
	public void updatingResumenImageById(String nombre, String ruta, int id) {
		// TODO Auto-generated method stub
		resumenRepository.updatingResumenImageById(nombre, ruta, id);
	}

}
