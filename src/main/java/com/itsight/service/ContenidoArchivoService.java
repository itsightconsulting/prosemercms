package com.itsight.service;

import java.util.List;

import com.itsight.domain.ContenidoArchivo;

public interface ContenidoArchivoService {

	List<ContenidoArchivo> listAll();
	
	List<ContenidoArchivo> findAllByContenidoWebId(int contenidoWebId);
	
	List<ContenidoArchivo> findByContenidoWebId(int contenidoWebId);
	
	ContenidoArchivo add(ContenidoArchivo contenidoArchivo);
	
	ContenidoArchivo update(ContenidoArchivo contenidoArchivo);
	
	void delete(int contenidoArchivoId);
	
	ContenidoArchivo getContenidoArchivoById(int contenidoArchivoId);
		
	String getAliasByContenidoWebIdAndUuid(int contenidoWebId, String uuid);

}
