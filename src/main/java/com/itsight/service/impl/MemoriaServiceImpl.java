package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Memoria;
import com.itsight.repository.MemoriaRepository;
import com.itsight.service.MemoriaService;

@Service
@Transactional
public class MemoriaServiceImpl implements MemoriaService {

	private MemoriaRepository memoriaRepository;
	
	@Autowired
	public MemoriaServiceImpl(MemoriaRepository memoriaRepository) {
		// TODO Auto-generated constructor stub
		this.memoriaRepository = memoriaRepository;
	}
	
	@Override
	public List<Memoria> listAll() {
		// TODO Auto-generated method stub
		return memoriaRepository.findByOrderByIdAsc();
	}

	@Override
	public Memoria add(Memoria memoria) {
		// TODO Auto-generated method stub
		return memoriaRepository.save(memoria);
	}

	@Override
	public Memoria update(Memoria memoria) {
		// TODO Auto-generated method stub
		return memoriaRepository.saveAndFlush(memoria);
	}

	@Override
	public Memoria findOneById(int id) {
		// TODO Auto-generated method stub
		return memoriaRepository.findOne(new Integer(id));
	}

	@Override
	public String getNombreArchivoById(int id) {
		// TODO Auto-generated method stub
		return memoriaRepository.getNombreArchivoById(id);
	}

	@Override
	public Integer findContenidoIdById(int id) {
		// TODO Auto-generated method stub
		return memoriaRepository.findContenidoIdById(id);
	}

}
