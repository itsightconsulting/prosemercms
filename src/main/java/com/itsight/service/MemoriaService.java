package com.itsight.service;

import java.util.List;

import com.itsight.domain.Memoria;

public interface MemoriaService {

	List<Memoria> listAll();
	
	Memoria add(Memoria memoria);
	
	Memoria update(Memoria memoria);
	
	Memoria findOneById(int id);

	String getNombreArchivoById(int id);

	Integer findContenidoIdById(int id);
	
}
