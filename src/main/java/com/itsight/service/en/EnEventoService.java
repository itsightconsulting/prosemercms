package com.itsight.service.en;

import java.util.Date;
import java.util.List;

import com.itsight.domain.en.EnEvento;

public interface EnEventoService {

	List<EnEvento> listAll();
	
	EnEvento add(EnEvento evento);
	
	EnEvento update(EnEvento evento);
	
	void delete(int eventoId);
	
	EnEvento findOneById(int eventoId);

	EnEvento getEventoByContenidoWebId(int contenidoWebId);

	List<EnEvento> findAllByFlagActivo(Boolean flagActivo);

	List<EnEvento> findAllByTituloPrincipalContainingIgnoreCase(String comodin);
	
	List<Date> findDistinctFechaEvento();
	
	List<Integer> findAllIdsByFechaEvento(Date fechaEvento);

	EnEvento findOneWithContenidoWebById(int eventoId);

	Integer findContenidoIdById(int eventoId);

	void updateStatusById(boolean flagActivo, int id);

    List<String> findAllDistinctTags();
}
