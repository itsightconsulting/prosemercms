package com.itsight.repository.en;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnContenidoWeb;

@Repository
public interface EnContenidoWebRepository extends JpaRepository<EnContenidoWeb, Integer>{

	@Override
    @EntityGraph(value="enContenidoWeb.usuario", attributePaths = {})
    List<EnContenidoWeb> findAll();
	
	@EntityGraph(value="contenidoWeb", attributePaths = {})
	List<EnContenidoWeb> findAllByFlagActivo(Boolean flagActivo);
	
    @Query("SELECT NEW EnContenidoWeb(id, titulo, url) FROM EnContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<EnContenidoWeb> findAllByTipoContenidoId(int tipoContenidoId);
    
    @Query("SELECT NEW EnContenidoWeb(titulo, url, id, resumen.resumen) FROM EnContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido ASC, 2 ASC ")
    List<EnContenidoWeb> findWithResumenByTipoContenidoId(int tipoContenidoId);
    
    @Query("SELECT NEW EnContenidoWeb(id, titulo, url, tags) FROM EnContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<EnContenidoWeb> findAllTagsByTipoContenidoId(int tipoContenidoId);

    @Query("SELECT NEW EnContenidoWeb(id, titulo, flagInicio) FROM EnContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<EnContenidoWeb> findAllForInicioByTipoContenidoId(int tipoContenidoId);
    
    @Query("SELECT NEW EnContenidoWeb(id, titulo, url) FROM EnContenidoWeb C WHERE C.flagActivo = true AND C.tipoContenido.id = ?1 AND LOWER(C.titulo) LIKE LOWER(concat('%', ?2, '%')) ORDER BY C.fechaContenido DESC, 2 ASC")
	List<EnContenidoWeb> findAllByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo);
    
    @Query("SELECT NEW EnContenidoWeb(id, titulo, flagInicio) FROM EnContenidoWeb C WHERE C.tipoContenido.id = ?1 AND LOWER(C.titulo) LIKE LOWER(concat('%', ?2, '%')) ORDER BY C.fechaContenido DESC, 2 ASC")
	List<EnContenidoWeb> findAllForInicioByTipoContenidoIdAndTituloIgnoreCase(int tipoContenidoId, String titulo);
	
	@EntityGraph(value="enContenidoWeb.usuario", attributePaths = {})
	List<EnContenidoWeb> findAllByTituloContaining(String titulo);
	
    @EntityGraph(value="enContenidoWeb.all", attributePaths = {})
	EnContenidoWeb findContenidoWebById(Integer contenidoWebId);
    
    @Query("SELECT NEW EnContenidoWeb(id, titulo, url) FROM EnContenidoWeb C WHERE C.id IN ?1 ORDER BY C.fechaContenido DESC, 2 ASC ")
    List<EnContenidoWeb> findAllByIdIn(List<Integer> ids);
    
    @Query("SELECT NEW EnContenidoWeb(id, titulo, flagInicio) FROM EnContenidoWeb C WHERE C.id IN ?1 ORDER BY 1 DESC, 2 ASC")
    List<EnContenidoWeb> findAllForInicioByIdIn(List<Integer> ids);
    
    @Query("SELECT C.tags FROM EnContenidoWeb C WHERE C.id = ?1")
    String findTagsById(int contenidoWebId);
    
    @Query("UPDATE EnContenidoWeb SET flagActivo = ?2  WHERE id = ?1")
	@Modifying
	void updatingFlagActivoById(int id, boolean status);
    
    @Query("UPDATE EnContenidoWeb SET flagInicio = ?2  WHERE id = ?1")
	@Modifying
	void updatingFlagInicioById(int id, boolean status);
    
    @Query("SELECT NEW EnContenidoWeb(titulo, url, id, resumen.resumen) FROM EnContenidoWeb C WHERE C.flagActivo = true AND C.tipoContenido.id = ?1 AND LOWER(C.titulo) LIKE LOWER(concat('%', ?2, '%')) ORDER BY C.fechaContenido DESC, 2 ASC")
	List<EnContenidoWeb> findWithResumenByTipoContenidoIdAndTitulo(int tipoContenidoId, String titulo);

    @Query("SELECT NEW EnContenidoWeb(titulo, url, id, resumen.resumen) FROM EnContenidoWeb C WHERE C.id IN ?1 ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<EnContenidoWeb> findWithResumenAllByIdIn(List<Integer> ids);

    @Query("SELECT NEW EnContenidoWeb(id, titulo, url, tags, resumen.resumen) FROM EnContenidoWeb C WHERE C.tipoContenido.id = ?1 AND C.flagActivo = true ORDER BY C.fechaContenido DESC, 2 ASC ")
	List<EnContenidoWeb> findWithResumenTagsByTipoContenidoId(int tipoContenidoId);
	
}
