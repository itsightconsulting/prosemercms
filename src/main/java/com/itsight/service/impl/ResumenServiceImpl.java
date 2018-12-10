package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Resumen;
import com.itsight.repository.ResumenRepository;
import com.itsight.service.ResumenService;

@Service
@Transactional
public class ResumenServiceImpl implements ResumenService {

	
	private ResumenRepository resumenRepository;
	
	@Autowired
	ResumenServiceImpl(ResumenRepository resumenRepository){
		this.resumenRepository = resumenRepository;
	}
	
	@Override
	public List<Resumen> listAll() {
		// TODO Auto-generated method stub
		return resumenRepository.findAll();
	}

	@Override
	public Resumen getResumenById(int ResumenId) {
		// TODO Auto-generated method stub
		return resumenRepository.findOne(new Integer(ResumenId));
	}
	@Override
	public List<Resumen> findAllByIdIn(Integer[] ids) {
		// TODO Auto-generated method stub
		return resumenRepository.findAllByIdIn(ids);
	}

	@Override
	public Resumen findResumenById(Integer contenidoWebId) {
		// TODO Auto-generated method stub
		return resumenRepository.findResumenById(contenidoWebId);
	}
	

	@Override
	public Resumen update(Resumen resumen) {
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
		resumenRepository.updatingResumenImageById(nombre, ruta, id);
	}
}
