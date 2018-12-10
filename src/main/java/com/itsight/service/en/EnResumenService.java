package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnResumen;

public interface EnResumenService {

	List<EnResumen> listAll();
	
	EnResumen update(EnResumen resumen);

	void delete(int contenidoWebId);
	
	EnResumen getResumenById(int contenidoWebId);
			
	List<EnResumen> findAllByIdIn(Integer[] ids);

	EnResumen findResumenById(Integer contenidoWebId);
	
    void updatingResumenImageById(String nombre, String ruta, int id);
	
}
