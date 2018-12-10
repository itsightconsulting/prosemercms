package com.itsight.service.en.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnQuienesSomos;
import com.itsight.repository.en.EnQuienesSomosRepository;
import com.itsight.service.en.EnQuienesSomosService;

@Service
@Transactional
public class EnQuienesSomosServiceImpl implements EnQuienesSomosService {

	private EnQuienesSomosRepository quienesSomosRepository;
	
	@Autowired
	public EnQuienesSomosServiceImpl(EnQuienesSomosRepository quienesSomosRepository) {
		// TODO Auto-generated constructor stub
		this.quienesSomosRepository = quienesSomosRepository;
	}
	
	@Override
	public List<EnQuienesSomos> listAll() {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findByOrderByIdAsc();
	}

	@Override
	public EnQuienesSomos add(EnQuienesSomos quienesSomos) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.save(quienesSomos);
	}

	@Override
	public EnQuienesSomos update(EnQuienesSomos quienesSomos) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.saveAndFlush(quienesSomos);
	}

	@Override
	public EnQuienesSomos findOneById(int id) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findOne(new Integer(id));
	}

	@Override
	public EnQuienesSomos findByNombreMenuContaining(String nombreMenu) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findByNombreMenuContaining(nombreMenu);
	}

	@Override
	public Integer findContenidoIdById(int quienesSomosId) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findContenidoIdById(quienesSomosId);
	}

}
