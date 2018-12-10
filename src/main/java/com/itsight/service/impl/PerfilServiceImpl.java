package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Perfil;
import com.itsight.repository.PerfilRepository;
import com.itsight.service.PerfilService;

@Service
@Transactional
public class PerfilServiceImpl implements PerfilService {
	
	private PerfilRepository perfilRepository;
	
	@Autowired
	public PerfilServiceImpl(PerfilRepository perfilRepository) {
		this.perfilRepository = perfilRepository;
	}

	@Override
	public List<Perfil> listAll() {
		// TODO Auto-generated method stub
		return perfilRepository.findAll();
	}

	@Override
	public Perfil add(Perfil perfil) {
		// TODO Auto-generated method stub
		return perfilRepository.save(perfil);
	}

	@Override
	public Perfil update(Perfil perfil) {
		// TODO Auto-generated method stub
		return perfilRepository.saveAndFlush(perfil);
	}

	@Override
	public void delete(int perfilId) {
		// TODO Auto-generated method stub
		perfilRepository.delete(new Integer(perfilId));
	}

	@Override
	public Perfil findOneById(int perfilId) {
		// TODO Auto-generated method stub
		return perfilRepository.findOne(new Integer(perfilId));
	}

}
