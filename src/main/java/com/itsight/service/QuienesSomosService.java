package com.itsight.service;

import java.util.List;

import com.itsight.domain.QuienesSomos;

public interface QuienesSomosService {

	List<QuienesSomos> listAll();
	
	QuienesSomos add(QuienesSomos quienesSomos);
	
	QuienesSomos update(QuienesSomos quienesSomos);
	
	QuienesSomos findOneById(int id);
	
	QuienesSomos findByNombreMenuContaining(String nombreMenu);

	Integer findContenidoIdById(int quienesSomosId);

}
