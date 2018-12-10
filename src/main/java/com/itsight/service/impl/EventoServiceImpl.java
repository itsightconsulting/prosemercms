package com.itsight.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Evento;
import com.itsight.repository.EventoRepository;
import com.itsight.service.EventoService;

@Service
@Transactional
public class EventoServiceImpl implements EventoService {
	
	private EventoRepository eventoRepository;
	
	@Autowired
	public EventoServiceImpl(EventoRepository eventoRepository) {
		this.eventoRepository = eventoRepository;
	}

	@Override
	public List<Evento> listAll() {
		// TODO Auto-generated method stub
		return eventoRepository.findAllByOrderByFechaEventoAscTituloPrincipalAsc();
	}
	@Override
	public List<Evento> findAllByTituloPrincipalContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return eventoRepository.findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEventoAscTituloPrincipalAsc(comodin);
	}

	@Override
	public List<Evento> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return eventoRepository.findAllByFlagActivoOrderByFechaEventoAscTituloPrincipalAsc(flagActivo);
	}
	
	@Override
	public List<Date> findDistinctFechaEvento() {
		// TODO Auto-generated method stub
		return eventoRepository.findDistinctFechaEvento();
	}

	@Override
	public List<Integer> findAllIdsByFechaEvento(Date fechaEvento) {
		// TODO Auto-generated method stub
		return eventoRepository.findAllIdsByFechaEvento(fechaEvento);
	}

	@Override
	public Evento add(Evento evento) {
		// TODO Auto-generated method stub
		return eventoRepository.save(evento);
	}

	@Override
	public Evento update(Evento evento) {
		// TODO Auto-generated method stub
		return eventoRepository.saveAndFlush(evento);
	}

	@Override
	public void delete(int eventoId) {
		// TODO Auto-generated method stub
		eventoRepository.delete(new Integer(eventoId));
	}

	@Override
	public Evento findOneById(int eventoId) {
		// TODO Auto-generated method stub
		return eventoRepository.findOne(new Integer(eventoId));
	}

	@Override
	public Evento getEventoByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return eventoRepository.findEventoByContenidoWebId(contenidoWebId);
	}

	@Override
	public Evento findOneWithContenidoWebById(int eventoId) {
		// TODO Auto-generated method stub
		return eventoRepository.findOneWithContenidoWebById(eventoId);
	}

	@Override
	public Integer findContenidoIdById(int eventoId) {
		// TODO Auto-generated method stub
		return eventoRepository.findContenidoIdById(eventoId);
	}

	@Override
	public List<String> findAllDistinctTags() {
		return eventoRepository.findAllDistinctTags();
	}
}
