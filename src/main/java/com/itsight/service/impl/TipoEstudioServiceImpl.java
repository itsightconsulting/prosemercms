package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.TipoEstudio;
import com.itsight.repository.TipoEstudioRepository;
import com.itsight.service.TipoEstudioService;

@Service
@Transactional
public class TipoEstudioServiceImpl implements TipoEstudioService {
	
	private TipoEstudioRepository tipoEstudioRepository;
	
	@Autowired
	public TipoEstudioServiceImpl(TipoEstudioRepository tipoEstudioRepository) {
		this.tipoEstudioRepository = tipoEstudioRepository;
	}

	@Override
	public List<TipoEstudio> listAll() {
		// TODO Auto-generated method stub
		return tipoEstudioRepository.findAll();
	}

	@Override
	public TipoEstudio add(TipoEstudio tipoEstudio) {
		// TODO Auto-generated method stub
		return tipoEstudioRepository.save(tipoEstudio);
	}

	@Override
	public TipoEstudio update(TipoEstudio tipoEstudio) {
		// TODO Auto-generated method stub
		return tipoEstudioRepository.saveAndFlush(tipoEstudio);
	}

	@Override
	public void delete(int tipoEstudioId) {
		// TODO Auto-generated method stub
		tipoEstudioRepository.delete(new Integer(tipoEstudioId));
	}

	@Override
	public TipoEstudio findOneById(int tipoEstudioId) {
		// TODO Auto-generated method stub
		return tipoEstudioRepository.findOne(new Integer(tipoEstudioId));
	}

}
