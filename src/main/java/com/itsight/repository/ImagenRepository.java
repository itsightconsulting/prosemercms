package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.free.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Integer> {

	@Override
    @EntityGraph(value="contenidoImagen", attributePaths = {})
    List<Imagen> findAll();
	
	@Query("SELECT new Imagen(nombreMedia, rutaMediaWeb) FROM Imagen I WHERE I.id = ?1 ORDER BY I.id")
    List<Imagen> findImagenById(int id);
	
	@Query("SELECT new Imagen(id, nombreMedia, rutaMediaWeb) FROM Imagen I WHERE I.id = ?1 ORDER BY 1")
    List<Imagen> findAllById(int id);
}
