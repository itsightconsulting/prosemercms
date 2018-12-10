package com.itsight.service;

import java.util.Date;
import java.util.List;

import com.itsight.domain.Evento;

public interface EventoService {

	List<Evento> listAll();
	
	Evento add(Evento evento);
	
	Evento update(Evento evento);
	
	void delete(int eventoId);
	
	Evento findOneById(int eventoId);

	Evento getEventoByContenidoWebId(int contenidoWebId);

	List<Evento> findAllByFlagActivo(Boolean flagActivo);

	List<Evento> findAllByTituloPrincipalContainingIgnoreCase(String comodin);
	
	List<Date> findDistinctFechaEvento();
	
	List<Integer> findAllIdsByFechaEvento(Date fechaEvento);

	Evento findOneWithContenidoWebById(int eventoId);

	Integer findContenidoIdById(int eventoId);

    List<String> findAllDistinctTags();
}
