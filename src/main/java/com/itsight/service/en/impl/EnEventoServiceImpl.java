package com.itsight.service.en.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnEvento;
import com.itsight.repository.en.EnEventoRepository;
import com.itsight.service.en.EnEventoService;

@Service
@Transactional
public class EnEventoServiceImpl implements EnEventoService {
	
	private EnEventoRepository eventoRepository;
	
	@Autowired
	public EnEventoServiceImpl(EnEventoRepository eventoRepository) {
		this.eventoRepository = eventoRepository;
	}

	@Override
	public List<EnEvento> listAll() {
		// TODO Auto-generated method stub
		return eventoRepository.findAllByOrderByFechaEventoAscTituloPrincipalAsc();
	}
	@Override
	public List<EnEvento> findAllByTituloPrincipalContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return eventoRepository.findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEventoAscTituloPrincipalAsc(comodin);
	}

	@Override
	public List<EnEvento> findAllByFlagActivo(Boolean flagActivo) {
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
	public EnEvento add(EnEvento evento) {
		// TODO Auto-generated method stub
		return eventoRepository.save(evento);
	}

	@Override
	public EnEvento update(EnEvento evento) {
		// TODO Auto-generated method stub
		return eventoRepository.saveAndFlush(evento);
	}

	@Override
	public void delete(int eventoId) {
		// TODO Auto-generated method stub
		eventoRepository.delete(new Integer(eventoId));
	}

	@Override
	public EnEvento findOneById(int eventoId) {
		// TODO Auto-generated method stub
		return eventoRepository.findOne(new Integer(eventoId));
	}

	@Override
	public EnEvento getEventoByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return eventoRepository.findEventoByContenidoWebId(contenidoWebId);
	}

	@Override
	public EnEvento findOneWithContenidoWebById(int eventoId) {
		// TODO Auto-generated method stub
		return eventoRepository.findOneWithContenidoWebById(eventoId);
	}

	@Override
	public Integer findContenidoIdById(int eventoId) {
		// TODO Auto-generated method stub
		return eventoRepository.findContenidoIdById(eventoId);
	}

	@Override
	public void updateStatusById(boolean flagActivo, int id) {
		// TODO Auto-generated method stub
		eventoRepository.updateStatusById(flagActivo, id);
	}

	@Override
	public List<String> findAllDistinctTags() {
		return eventoRepository.findAllDistinctTags();
	}
}
