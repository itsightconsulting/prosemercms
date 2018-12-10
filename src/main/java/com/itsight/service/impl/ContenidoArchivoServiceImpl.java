package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.ContenidoArchivo;
import com.itsight.repository.ContenidoArchivoRepository;
import com.itsight.service.ContenidoArchivoService;

@Service
@Transactional
public class ContenidoArchivoServiceImpl implements ContenidoArchivoService {

	
	private ContenidoArchivoRepository contenidoArchivoRepository;
	
	@Autowired
	public ContenidoArchivoServiceImpl(ContenidoArchivoRepository contenidoArchivoRepository) {
		this.contenidoArchivoRepository = contenidoArchivoRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<ContenidoArchivo> listAll() {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.findAll();
	}

	@Override
	public List<ContenidoArchivo> findAllByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.findAllByContenidoWebId(contenidoWebId);
	}

	@Override
	public List<ContenidoArchivo> findByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.findByContenidoWebId(contenidoWebId);
	}

	@Override
	public ContenidoArchivo getContenidoArchivoById(int contenidoArchivoId) {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.findOne(new Integer(contenidoArchivoId));
	}

	@Override
	public ContenidoArchivo add(ContenidoArchivo contenidoArchivo) {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.save(contenidoArchivo);
	}

	@Override
	public ContenidoArchivo update(ContenidoArchivo contenidoArchivo) {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.saveAndFlush(contenidoArchivo);
	}

	@Override
	public void delete(int contenidoArchivoId) {
		// TODO Auto-generated method stub
		contenidoArchivoRepository.delete(new Integer(contenidoArchivoId));
	}

	@Override
	public String getAliasByContenidoWebIdAndUuid(int contenidoWebId, String uuid) {
		// TODO Auto-generated method stub
		return contenidoArchivoRepository.findAliasByContenidoWebIdAndUuid(contenidoWebId, uuid);
	}
}
