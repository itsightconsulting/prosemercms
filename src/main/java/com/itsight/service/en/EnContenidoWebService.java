package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnContenidoWeb;

public interface EnContenidoWebService {

	List<EnContenidoWeb> listAll();
	
	EnContenidoWeb add(EnContenidoWeb contenidoWeb);
	
	EnContenidoWeb update(EnContenidoWeb contenidoWeb);
	
	void delete(int contenidoWebId);
	
	EnContenidoWeb findOneById(int id);
	
	EnContenidoWeb getContenidoWebById(int contenidoWebId);
	
	List<EnContenidoWeb> findAllByFlagActivo(Boolean flagActivo);
	
	List<EnContenidoWeb> findAllByTituloContaining(String titulo);
		
	List<EnContenidoWeb> findAllByTipoContenidoId(int tipoContenidoId);
	
	List<EnContenidoWeb> findWithResumenByTipoContenidoId(int tipoContenidoId);
    
	List<EnContenidoWeb> findAllByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo);
	
	List<EnContenidoWeb> findAllByIdIn(List<Integer> ids);
	
    List<EnContenidoWeb> findAllForInicioByIdIn(List<Integer> ids);

	EnContenidoWeb findContenidoWebById(Integer contenidoWebId);
	
	void updatingFlagActivoById(int id, boolean status);
	
	void updatingFlagInicioById(int id, boolean status);

	List<EnContenidoWeb> findAllTagsByTipoContenidoId(int tipoContenidoId);
	
	List<EnContenidoWeb> findAllForInicioByTipoContenidoId(int tipoContenidoId);
	
	List<EnContenidoWeb> findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo);
	
    String findTagsById(int contenidoWebId);
	
	List<EnContenidoWeb> findWithResumenTagsByTipoContenidoId(int tipoContenidoId);
	
	List<EnContenidoWeb> findWithResumenAllByIdIn(List<Integer> ids);
	
	List<EnContenidoWeb> findWithResumenByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo);

}
