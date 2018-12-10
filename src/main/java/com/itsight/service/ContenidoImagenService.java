package com.itsight.service;

import java.util.List;

import com.itsight.domain.ContenidoImagen;

public interface ContenidoImagenService {

	List<ContenidoImagen> listAll();
	
	List<ContenidoImagen> findByContenidoWebId(int contenidoWebId);
	
    List<ContenidoImagen> findAllByContenidoWebId(int contenidoWebId);
	
	ContenidoImagen add(ContenidoImagen contenidoImagen);
	
	ContenidoImagen update(ContenidoImagen contenidoImagen);
	
	void delete(int contenidoImagenId);
	
	ContenidoImagen getContenidoImagenById(int contenidoImagenId);
		
}
