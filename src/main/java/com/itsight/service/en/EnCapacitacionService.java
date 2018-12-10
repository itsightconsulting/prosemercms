package com.itsight.service.en;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.itsight.domain.en.EnCapacitacion;

public interface EnCapacitacionService {

	List<EnCapacitacion> listAll();
	
	List<EnCapacitacion> findAllByFlagActivo(Boolean flagActivo);

	List<EnCapacitacion> findAllByTituloPrincipalContainingIgnoreCase(String comodin);
		
	EnCapacitacion add(EnCapacitacion capacitacion);
	
	EnCapacitacion update(EnCapacitacion capacitacion);
	
	void delete(int capacitacionId);
	
	EnCapacitacion findOneById(int capacitacionId);

	EnCapacitacion getCapacitacionByContenidoWebId(int contenidoWebId);	
	
	Integer findContenidoIdById(int id);

	List<Date> findDistinctFechaCapacitacion() throws ParseException;
		
	List<Integer> findAllIdsByFechaCapacitacion(Date fechaCapacitacion);

	void updateStatusById(boolean flagActivo, int id);

	void updateTakeOffRelacionEstudio(int id);

    List<String> findAllDistinctTags();
}
