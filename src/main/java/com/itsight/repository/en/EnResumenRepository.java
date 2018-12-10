package com.itsight.repository.en;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnResumen;

@Repository
public interface EnResumenRepository extends JpaRepository<EnResumen, Integer>{

	@Override
    @EntityGraph(value="enResumen.contenidoWeb", attributePaths = {})
    List<EnResumen> findAll();
	
    @EntityGraph(value="enResumen", attributePaths = {})
	EnResumen findResumenById(Integer resumenId);
    
    @Query("SELECT NEW EnResumen(id, titulo,resumen, url, rutaImagenPortada) FROM EnResumen R WHERE R.contenidoWeb.id IN ?1 ORDER BY 1 DESC")
    List<EnResumen> findAllByIdIn(Integer[] ids);
    
    @Modifying
    @Query("UPDATE EnResumen E SET E.nombreImagenPortada = ?1, E.rutaImagenPortada = ?2 WHERE E.id = ?3")
    void updatingResumenImageById(String nombre, String ruta, int id);
	
}
