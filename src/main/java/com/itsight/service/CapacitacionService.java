package com.itsight.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.itsight.domain.Capacitacion;

public interface CapacitacionService {

	List<Capacitacion> listAll();
	
	List<Capacitacion> findAllByFlagActivo(Boolean flagActivo);

	List<Capacitacion> findAllByTituloPrincipalContainingIgnoreCase(String comodin);
		
	Capacitacion add(Capacitacion capacitacion);
	
	Capacitacion update(Capacitacion capacitacion);
	
	void delete(int capacitacionId);
	
	Capacitacion findOneById(int capacitacionId);

	Capacitacion getCapacitacionByContenidoWebId(int contenidoWebId);	
	
	Integer findContenidoIdById(int id);

	List<Date> findDistinctFechaCapacitacion() throws ParseException;
		
	List<Integer> findAllIdsByFechaCapacitacion(Date fechaCapacitacion);

	List<Integer> findIdsByEstudioId(int id);

	void updateTakeOffRelacionEstudio(int id);

    List<String> findAllDistinctTags();
}
