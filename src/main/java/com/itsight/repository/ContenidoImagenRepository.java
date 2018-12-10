package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.ContenidoImagen;

@Repository
public interface ContenidoImagenRepository extends JpaRepository<ContenidoImagen, Integer> {

	@Override
    @EntityGraph(value="contenidoImagen.contenidoWeb", attributePaths = {})
    List<ContenidoImagen> findAll();
	
	@Query("SELECT new ContenidoImagen(nombreMedia, rutaMediaWeb) FROM ContenidoImagen CI WHERE CI.contenidoWeb.id = ?1 ORDER BY CI.id")
    List<ContenidoImagen> findImagenByContenidoWebId(int contenidoWebId);
	
	@Query("SELECT new ContenidoImagen(id, nombreMedia, rutaMediaWeb) FROM ContenidoImagen CI WHERE CI.contenidoWeb.id = ?1 ORDER BY 1")
    List<ContenidoImagen> findAllByContenidoWebId(int contenidoWebId);
}
