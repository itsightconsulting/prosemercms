package com.itsight.service;

import java.util.List;

import com.itsight.domain.TipoImagen;

public interface TipoImagenService {

	List<TipoImagen> listAll();
	
	TipoImagen add(TipoImagen tipoImagen);
	
	TipoImagen update(TipoImagen tipoImagen);
	
	TipoImagen findOneById(int tipoImagenId);
	
	void delete(int tipoImagenId);
	
}
