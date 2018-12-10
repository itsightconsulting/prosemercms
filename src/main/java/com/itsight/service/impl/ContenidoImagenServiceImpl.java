package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.ContenidoImagen;
import com.itsight.repository.ContenidoImagenRepository;
import com.itsight.service.ContenidoImagenService;

@Service
@Transactional
public class ContenidoImagenServiceImpl implements ContenidoImagenService {

	
	private ContenidoImagenRepository contenidoImagenRepository;
	
	@Autowired
	public ContenidoImagenServiceImpl(ContenidoImagenRepository contenidoImagenRepository) {
		this.contenidoImagenRepository = contenidoImagenRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<ContenidoImagen> listAll() {
		// TODO Auto-generated method stub
		return contenidoImagenRepository.findAll();
	}

	@Override
	public List<ContenidoImagen> findAllByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoImagenRepository.findAllByContenidoWebId(contenidoWebId);
	}

	@Override
	public List<ContenidoImagen> findByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoImagenRepository.findImagenByContenidoWebId(contenidoWebId);
	}

	@Override
	public ContenidoImagen getContenidoImagenById(int contenidoImagenId) {
		// TODO Auto-generated method stub
		return contenidoImagenRepository.findOne(new Integer(contenidoImagenId));
	}

	@Override
	public ContenidoImagen add(ContenidoImagen contenidoImagen) {
		// TODO Auto-generated method stub
		return contenidoImagenRepository.save(contenidoImagen);
	}

	@Override
	public ContenidoImagen update(ContenidoImagen contenidoImagen) {
		// TODO Auto-generated method stub
		return contenidoImagenRepository.saveAndFlush(contenidoImagen);
	}

	@Override
	public void delete(int contenidoImagenId) {
		// TODO Auto-generated method stub
		contenidoImagenRepository.delete(new Integer(contenidoImagenId));
	}

}
