package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Parametro;
import com.itsight.repository.ParametroRepository;
import com.itsight.service.ParametroService;

@Service
@Transactional
public class ParametroServiceImpl implements ParametroService {
	
	private ParametroRepository parametroRepository;
	
	@Autowired
	public ParametroServiceImpl(ParametroRepository parametroRepository) {
		this.parametroRepository = parametroRepository;
	}

	@Override
	public List<Parametro> listAll() {
		// TODO Auto-generated method stub
		return parametroRepository.findAll();
	}

	@Override
	public Parametro add(Parametro parametro) {
		// TODO Auto-generated method stub
		return parametroRepository.save(parametro);
	}

	@Override
	public Parametro update(Parametro parametro) {
		// TODO Auto-generated method stub
		return parametroRepository.saveAndFlush(parametro);
	}

	@Override
	public void delete(int parametroId) {
		// TODO Auto-generated method stub
		parametroRepository.delete(new Integer(parametroId));
	}

	@Override
	public Parametro getParametroById(int parametroId) {
		// TODO Auto-generated method stub
		return parametroRepository.findOne(new Integer(parametroId));
	}

	@Override
	public Parametro findByClave(String clave) {
		// TODO Auto-generated method stub
		return parametroRepository.findByClave(clave);
	}

	@Override
	public List<Parametro> findAllByClave(String clave) {
		// TODO Auto-generated method stub
		return parametroRepository.findAllByClaveContaining(clave);
	}

	@Override
	public List<Parametro> findAllByClaveContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return parametroRepository.findAllByClaveContainingIgnoreCase(comodin);
	}
}
