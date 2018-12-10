package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.ContenidoWeb;

@Repository
public interface ContenidoWebRepository extends JpaRepository<ContenidoWeb, Integer>{

	@Override
    @EntityGraph(value="contenidoWeb.usuario", attributePaths = {})
    List<ContenidoWeb> findAll();
	
	@EntityGraph(value="contenidoWeb", attributePaths = {})
	List<ContenidoWeb> findAllByFlagActivo(Boolean flagActivo);
	
    @Query("SELECT NEW ContenidoWeb(id, titulo, url) FROM ContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<ContenidoWeb> findAllByTipoContenidoId(int tipoContenidoId);
    
    @Query("SELECT NEW ContenidoWeb(titulo, url, id, resumen.resumen) FROM ContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido ASC, 2 ASC ")
	List<ContenidoWeb> findWithResumenByTipoContenidoId(int tipoContenidoId);
    
    @Query("SELECT NEW ContenidoWeb(id, titulo, url, tags) FROM ContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<ContenidoWeb> findAllTagsByTipoContenidoId(int tipoContenidoId);

    @Query("SELECT NEW ContenidoWeb(id, titulo, flagInicio) FROM ContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<ContenidoWeb> findAllForInicioByTipoContenidoId(int tipoContenidoId);
    
    @Query("SELECT NEW ContenidoWeb(id, titulo, url) FROM ContenidoWeb C WHERE C.flagActivo = true AND C.tipoContenido.id = ?1 AND LOWER(C.titulo) LIKE LOWER(concat('%', ?2, '%')) ORDER BY C.fechaContenido DESC, 2 ASC")
	List<ContenidoWeb> findAllByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo);
    
    @Query("SELECT NEW ContenidoWeb(id, titulo, flagInicio) FROM ContenidoWeb C WHERE C.flagActivo = true AND C.tipoContenido.id = ?1 AND LOWER(C.titulo) LIKE LOWER(concat('%', ?2, '%')) ORDER BY C.fechaContenido DESC, 2 ASC")
	List<ContenidoWeb> findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo);
	
	@EntityGraph(value="contenidoWeb.usuario", attributePaths = {})
	List<ContenidoWeb> findAllByTituloContaining(String titulo);
	
    @EntityGraph(value="contenidoWeb.all", attributePaths = {})
	ContenidoWeb findContenidoWebById(Integer contenidoWebId);
    
    @Query("SELECT NEW ContenidoWeb(id, titulo, url) FROM ContenidoWeb C WHERE C.id IN ?1 ORDER BY C.fechaContenido DESC, 2 ASC ")
    List<ContenidoWeb> findAllByIdIn(List<Integer> ids);
    
    @Query("SELECT NEW ContenidoWeb(id, titulo, flagInicio) FROM ContenidoWeb C WHERE C.id IN ?1 ORDER BY 1 DESC, 2 ASC")
    List<ContenidoWeb> findAllForInicioByIdIn(List<Integer> ids);
    
    @Query("SELECT C.tags FROM ContenidoWeb C WHERE C.id = ?1")
    String findTagsById(int contenidoWebId);
    
    @Query("UPDATE ContenidoWeb SET flagActivo = ?2  WHERE id = ?1")
	@Modifying
	void updatingFlagActivoById(int id, boolean status);
    
    @Query("UPDATE ContenidoWeb SET flagInicio = ?2  WHERE id = ?1")
	@Modifying
	void updatingFlagInicioById(int id, boolean status);

    @Query("SELECT NEW ContenidoWeb(titulo, url, id, resumen.resumen) FROM ContenidoWeb C WHERE C.flagActivo = true AND C.tipoContenido.id = ?1 AND LOWER(C.titulo) LIKE LOWER(concat('%', ?2, '%')) ORDER BY C.fechaContenido DESC, 2 ASC")
	List<ContenidoWeb> findWithResumenByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo);

    @Query("SELECT NEW ContenidoWeb(titulo, url, id, resumen.resumen) FROM ContenidoWeb C WHERE C.id IN ?1 ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<ContenidoWeb> findWithResumenAllByIdIn(List<Integer> ids);

    @Query("SELECT NEW ContenidoWeb(id, titulo, url, tags, resumen.resumen) FROM ContenidoWeb C WHERE C.flagActivo = true AND C.tipoContenido.id = ?1 ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<ContenidoWeb> findWithResumenTagsByTipoContenidoId(int tipoContenidoId);
	
}
