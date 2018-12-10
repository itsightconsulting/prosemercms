package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.free.Archivo;
import com.itsight.repository.ArchivoRepository;
import com.itsight.service.ArchivoService;

@Service
@Transactional
public class ArchivoServiceImpl implements ArchivoService {

	
	private ArchivoRepository archivoRepository;
	
	@Autowired
	public ArchivoServiceImpl(ArchivoRepository archivoRepository) {
		this.archivoRepository = archivoRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Archivo> listAll() {
		// TODO Auto-generated method stub
		return archivoRepository.findAll();
	}

	@Override
	public List<Archivo> findAllById(int Id) {
		// TODO Auto-generated method stub
		return archivoRepository.findAllById(Id);
	}

	@Override
	public List<Archivo> findById(int Id) {
		// TODO Auto-generated method stub
		return archivoRepository.findById(Id);
	}

	@Override
	public Archivo getArchivoById(int id) {
		// TODO Auto-generated method stub
		return archivoRepository.findOne(new Integer(id));
	}

	@Override
	public Archivo add(Archivo contenidoArchivo) {
		// TODO Auto-generated method stub
		return archivoRepository.save(contenidoArchivo);
	}

	@Override
	public Archivo update(Archivo contenidoArchivo) {
		// TODO Auto-generated method stub
		return archivoRepository.saveAndFlush(contenidoArchivo);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		archivoRepository.delete(new Integer(id));
	}

	@Override
	public String getAliasByIdAndUuid(int Id, String uuid) {
		// TODO Auto-generated method stub
		return archivoRepository.findAliasByIdAndUuid(Id, uuid);
	}
}
