package com.itsight.service;

import java.util.List;

import com.itsight.domain.free.Archivo;

public interface ArchivoService {

	List<Archivo> listAll();
	
	List<Archivo> findAllById(int Id);
	
	List<Archivo> findById(int Id);
	
	Archivo add(Archivo archivo);
	
	Archivo update(Archivo archivo);
	
	void delete(int archivoId);
	
	Archivo getArchivoById(int id);
		
	String getAliasByIdAndUuid(int Id, String uuid);

}
