package com.itsight.service;

import java.util.List;

import com.itsight.domain.Parametro;

public interface ParametroService {

	List<Parametro> listAll();
	
	Parametro add(Parametro parametro);
	
	Parametro update(Parametro parametro);
	
	void delete(int parametroId);
	
	Parametro getParametroById(int parametroId);
	
	Parametro findByClave(String clave);
		
	List<Parametro> findAllByClave(String clave);

	List<Parametro> findAllByClaveContainingIgnoreCase(String comodin);
	
}
