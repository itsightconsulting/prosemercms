package com.itsight.service.en.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnMemoria;
import com.itsight.repository.en.EnMemoriaRepository;
import com.itsight.service.en.EnMemoriaService;

@Service
@Transactional
public class EnMemoriaServiceImpl implements EnMemoriaService {

	private EnMemoriaRepository memoriaRepository;
	
	@Autowired
	public EnMemoriaServiceImpl(EnMemoriaRepository memoriaRepository) {
		// TODO Auto-generated constructor stub
		this.memoriaRepository = memoriaRepository;
	}
	
	@Override
	public List<EnMemoria> listAll() {
		// TODO Auto-generated method stub
		return memoriaRepository.findByOrderByIdAsc();
	}

	@Override
	public EnMemoria add(EnMemoria memoria) {
		// TODO Auto-generated method stub
		return memoriaRepository.save(memoria);
	}

	@Override
	public EnMemoria update(EnMemoria memoria) {
		// TODO Auto-generated method stub
		return memoriaRepository.saveAndFlush(memoria);
	}

	@Override
	public EnMemoria findOneById(int id) {
		// TODO Auto-generated method stub
		return memoriaRepository.findOne(new Integer(id));
	}

	@Override
	public String getNombreArchivoById(int id) {
		// TODO Auto-generated method stub
		return memoriaRepository.getNombreArchivoById(id);
	}

	@Override
	public void updatingPortadaImageById(String nombreImagenPortada, String rutaImagenPortada, int id) {
		// TODO Auto-generated method stub
		memoriaRepository.updatingPortadaImageById(nombreImagenPortada, rutaImagenPortada, id);
	}

}
