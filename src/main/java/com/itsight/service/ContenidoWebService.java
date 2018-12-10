package com.itsight.service;

import java.util.List;

import com.itsight.domain.ContenidoWeb;

public interface ContenidoWebService {

	List<ContenidoWeb> listAll();
	
	ContenidoWeb add(ContenidoWeb contenidoWeb);
	
	ContenidoWeb update(ContenidoWeb contenidoWeb);
	
	void delete(int contenidoWebId);
	
	ContenidoWeb findOneById(int id);
	
	ContenidoWeb getContenidoWebById(int contenidoWebId);
	
	List<ContenidoWeb> findAllByFlagActivo(Boolean flagActivo);
	
	List<ContenidoWeb> findAllByTituloContaining(String titulo);
		
	List<ContenidoWeb> findAllByTipoContenidoId(int tipoContenidoId);
	
	List<ContenidoWeb> findWithResumenByTipoContenidoId(int tipoContenidoId);
    
	List<ContenidoWeb> findAllByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo);
	
	List<ContenidoWeb> findWithResumenByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo);
	
	List<ContenidoWeb> findAllByIdIn(List<Integer> ids);
	
	List<ContenidoWeb> findWithResumenAllByIdIn(List<Integer> ids);
	
    List<ContenidoWeb> findAllForInicioByIdIn(List<Integer> ids);

	ContenidoWeb findContenidoWebById(Integer contenidoWebId);
	
	void updatingFlagActivoById(int id, boolean status);
	
	void updatingFlagInicioById(int id, boolean status);

	List<ContenidoWeb> findAllTagsByTipoContenidoId(int tipoContenidoId);
	
	List<ContenidoWeb> findWithResumenTagsByTipoContenidoId(int tipoContenidoId);
	
	List<ContenidoWeb> findAllForInicioByTipoContenidoId(int tipoContenidoId);
	
	List<ContenidoWeb> findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo);
	
    String findTagsById(int contenidoWebId);
	
}
