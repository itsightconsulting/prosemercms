package com.itsight.service;

import java.util.List;

import com.itsight.domain.Resumen;

public interface ResumenService {

	List<Resumen> listAll();
	
	Resumen update(Resumen resumen);

	void delete(int contenidoWebId);
	
	Resumen getResumenById(int contenidoWebId);
			
	List<Resumen> findAllByIdIn(Integer[] ids);

	Resumen findResumenById(Integer contenidoWebId);

	void updatingResumenImageById(String nombre, String ruta, int id);
}
