package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnMemoria;

public interface EnMemoriaService {

	List<EnMemoria> listAll();
	
	EnMemoria add(EnMemoria memoria);
	
	EnMemoria update(EnMemoria memoria);
	
	EnMemoria findOneById(int id);

	String getNombreArchivoById(int id);

	void updatingPortadaImageById(String nombreImagenPortada, String rutaImagenPortada, int id);
	
}
