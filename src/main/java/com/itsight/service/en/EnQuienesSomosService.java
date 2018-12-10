package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnQuienesSomos;

public interface EnQuienesSomosService {

	List<EnQuienesSomos> listAll();
	
	EnQuienesSomos add(EnQuienesSomos quienesSomos);
	
	EnQuienesSomos update(EnQuienesSomos quienesSomos);
	
	EnQuienesSomos findOneById(int id);
	
	EnQuienesSomos findByNombreMenuContaining(String nombreMenu);

	Integer findContenidoIdById(int quienesSomosId);

}
