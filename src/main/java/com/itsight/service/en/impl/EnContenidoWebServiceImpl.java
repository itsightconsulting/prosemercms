package com.itsight.service.en.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnContenidoWeb;
import com.itsight.repository.en.EnContenidoWebRepository;
import com.itsight.service.en.EnContenidoWebService;

@Service
@Transactional
public class EnContenidoWebServiceImpl implements EnContenidoWebService {

	
	private EnContenidoWebRepository contenidoWebRepository;
	
	@Autowired
	public EnContenidoWebServiceImpl(EnContenidoWebRepository contenidoWebRepository) {
		this.contenidoWebRepository = contenidoWebRepository;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<EnContenidoWeb> listAll() {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAll();
	}

	@Override
	public List<EnContenidoWeb> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByFlagActivo(flagActivo);
	}

	@Override
	public List<EnContenidoWeb> findAllByTituloContaining(
			String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByTituloContaining(titulo);
	}

	@Override
	public List<EnContenidoWeb> findAllByIdIn(List<Integer> ids) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByIdIn(ids);
	}
	
	@Override
	public List<EnContenidoWeb> findAllForInicioByIdIn(List<Integer> ids) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllForInicioByIdIn(ids);
	}

	@Override
	public EnContenidoWeb findContenidoWebById(Integer contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findContenidoWebById(contenidoWebId);
	}

	@Override
	public List<EnContenidoWeb> findAllByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<EnContenidoWeb> findWithResumenByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<EnContenidoWeb> findAllByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllByTipoContenidoIdAndTituloIgnoreCase(tipoContenidoId, titulo);
	}

	@Override
	public EnContenidoWeb getContenidoWebById(int EnContenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findOne(new Integer(EnContenidoWebId));
	}

	@Override
	public EnContenidoWeb findOneById(int id) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findOne(new Integer(id));
	}

	@Override
	public EnContenidoWeb add(EnContenidoWeb EnContenidoWeb) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.save(EnContenidoWeb);
	}

	@Override
	public EnContenidoWeb update(EnContenidoWeb EnContenidoWeb) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.saveAndFlush(EnContenidoWeb);
	}

	@Override
	public void delete(int EnContenidoWebId) {
		// TODO Auto-generated method stub
		contenidoWebRepository.delete(new Integer(EnContenidoWebId));
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
	public List<EnContenidoWeb> findAllTagsByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllTagsByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<EnContenidoWeb> findAllForInicioByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllForInicioByTipoContenidoId(tipoContenidoId);
	}

	@Override
	public List<EnContenidoWeb> findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(tipoContenidoId, titulo);
	}

	@Override
	public String findTagsById(int contenidoWebId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findTagsById(contenidoWebId);
	}

	@Override
	public List<EnContenidoWeb> findWithResumenByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenByTipoContenidoIdAndTitulo(tipoContenidoId, titulo);
	}

	@Override
	public List<EnContenidoWeb> findWithResumenAllByIdIn(List<Integer> ids) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenAllByIdIn(ids);
	}

	@Override
	public List<EnContenidoWeb> findWithResumenTagsByTipoContenidoId(int tipoContenidoId) {
		// TODO Auto-generated method stub
		return contenidoWebRepository.findWithResumenTagsByTipoContenidoId(tipoContenidoId);
	}
	
	
	
	
}
