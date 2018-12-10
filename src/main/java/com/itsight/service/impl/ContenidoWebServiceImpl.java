package com.itsight.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.ContenidoWeb;
import com.itsight.repository.ContenidoWebRepository;
import com.itsight.service.ContenidoWebService;

@Service
@Transactional
public class ContenidoWebServiceImpl implements ContenidoWebService {

	
	private ContenidoWebRepository contenidoWebRepository;
	
	@Autowired
	public ContenidoWebServiceImpl(ContenidoWebRepository contenidoWebRepository) {
		this.contenidoWebRepository = contenidoWebRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<ContenidoWeb> listAll() {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAll();
	}

	@Override
	public List<ContenidoWeb> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByFlagActivo(flagActivo);
	}

	@Override
	public List<ContenidoWeb> findAllByTituloContaining(
			String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByTituloContaining(titulo);
	}

	@Override
	public List<ContenidoWeb> findAllByIdIn(List<Integer> ids) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByIdIn(ids);
	}
	
	@Override
	public List<ContenidoWeb> findAllForInicioByIdIn(List<Integer> ids) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllForInicioByIdIn(ids);
	}

	@Override
	public ContenidoWeb findContenidoWebById(Integer contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findContenidoWebById(contenidoWebId);
	}

	@Override
	public List<ContenidoWeb> findAllByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<ContenidoWeb> findAllByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByTipoContenidoIdAndTituloIgnoreCase(tipoContenidoId, titulo);
	}

	@Override
	public List<ContenidoWeb> findWithResumenByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public ContenidoWeb getContenidoWebById(int ContenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findOne(new Integer(ContenidoWebId));
	}

	@Override
	public ContenidoWeb findOneById(int id) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findOne(new Integer(id));
	}

	@Override
	public ContenidoWeb add(ContenidoWeb ContenidoWeb) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.save(ContenidoWeb);
	}

	@Override
	public ContenidoWeb update(ContenidoWeb ContenidoWeb) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.saveAndFlush(ContenidoWeb);
	}

	@Override
	public void delete(int ContenidoWebId) {
		// TODO Auto-generated method stub
		contenidoWebRepository.delete(new Integer(ContenidoWebId));
	}

	@Override
	public void updatingFlagActivoById(int id, boolean status) {
		// TODO Auto-generated method stub
		contenidoWebRepository.updatingFlagActivoById(id, status);
	}

	@Override
	public void updatingFlagInicioById(int id, boolean status) {
		// TODO Auto-generated method stub
		contenidoWebRepository.updatingFlagInicioById(id, status);
	}

	@Override
	public List<ContenidoWeb> findAllTagsByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllTagsByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<ContenidoWeb> findAllForInicioByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllForInicioByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<ContenidoWeb> findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(tipoContenidoId, titulo);
	}

	@Override
	public String findTagsById(int contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findTagsById(contenidoWebId);
	}

	@Override
	public List<ContenidoWeb> findWithResumenByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenByTipoContenidoIdAndTitulo(tipoContenidoId, titulo);
	}

	@Override
	public List<ContenidoWeb> findWithResumenAllByIdIn(List<Integer> ids) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenAllByIdIn(ids);
	}

	@Override
	public List<ContenidoWeb> findWithResumenTagsByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenTagsByTipoContenidoId(tipoContenidoId);
	}
	
	
}
