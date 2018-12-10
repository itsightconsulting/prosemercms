package com.itsight.service;

import java.util.List;

import com.itsight.domain.free.Imagen;

public interface ImagenService {

	List<Imagen> listAll();
	
	List<Imagen> findById(int id);
	
    List<Imagen> findAllById(int id);
	
	Imagen add(Imagen imagen);
	
	Imagen update(Imagen imagen);
	
	void delete(int id);
	
	Imagen getImagenById(int id);
		
}
