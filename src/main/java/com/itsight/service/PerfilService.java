package com.itsight.service;

import java.util.List;

import com.itsight.domain.Perfil;

public interface PerfilService {

	List<Perfil> listAll();
	
	Perfil add(Perfil perfil);
	
	Perfil update(Perfil perfil);
	
	void delete(int perfilId);
	
	Perfil findOneById(int perfilId);
	
	
}
