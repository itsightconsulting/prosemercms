package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.ContenidoArchivo;

@Repository
public interface ContenidoArchivoRepository extends JpaRepository<ContenidoArchivo, Integer> {

	@Override
    @EntityGraph(value="contenidoArchivo.contenidoWeb", attributePaths = {})
    List<ContenidoArchivo> findAll();
    
    @Query("SELECT new ContenidoArchivo(alias, rutaMediaWeb, peso) FROM ContenidoArchivo CA WHERE CA.contenidoWeb.id = ?1 ORDER BY CA.id")
    List<ContenidoArchivo> findByContenidoWebId(int contenidoWebId);
    
    @Query("SELECT CA.alias FROM ContenidoArchivo CA WHERE CA.contenidoWeb.id = ?1 AND CA.uuid = ?2")
	String findAliasByContenidoWebIdAndUuid(Integer contenidoWebId, String uuid);
	
    @Query("SELECT new ContenidoArchivo(id, nombreMedia, rutaMediaWeb, peso) FROM ContenidoArchivo CA WHERE CA.contenidoWeb.id = ?1 ORDER BY 1")
    List<ContenidoArchivo> findAllByContenidoWebId(int contenidoWebId);
}
