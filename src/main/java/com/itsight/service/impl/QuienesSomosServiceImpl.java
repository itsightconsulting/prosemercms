package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.QuienesSomos;
import com.itsight.repository.QuienesSomosRepository;
import com.itsight.service.QuienesSomosService;

@Service
@Transactional
public class QuienesSomosServiceImpl implements QuienesSomosService {

	private QuienesSomosRepository quienesSomosRepository;
	
	@Autowired
	public QuienesSomosServiceImpl(QuienesSomosRepository quienesSomosRepository) {
		// TODO Auto-generated constructor stub
		this.quienesSomosRepository = quienesSomosRepository;
	}
	
	@Override
	public List<QuienesSomos> listAll() {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findByOrderByIdAsc();
	}

	@Override
	public QuienesSomos add(QuienesSomos quienesSomos) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.save(quienesSomos);
	}

	@Override
	public QuienesSomos update(QuienesSomos quienesSomos) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.saveAndFlush(quienesSomos);
	}

	@Override
	public QuienesSomos findOneById(int id) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findOne(new Integer(id));
	}

	@Override
	public QuienesSomos findByNombreMenuContaining(String nombreMenu) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findByNombreMenuContaining(nombreMenu);
	}

	@Override
	public Integer findContenidoIdById(int quienesSomosId) {
		// TODO Auto-generated method stub
		return quienesSomosRepository.findContenidoIdById(quienesSomosId);
	}

}
