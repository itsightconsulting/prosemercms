package com.itsight.service;

import java.util.List;

import com.itsight.domain.TipoEstudio;

public interface TipoEstudioService {

	List<TipoEstudio> listAll();
	
	TipoEstudio add(TipoEstudio tipoEstudio);
	
	TipoEstudio update(TipoEstudio tipoEstudio);
	
	void delete(int tipoEstudioId);
	
	TipoEstudio findOneById(int tipoEstudioId);
	
	
}
