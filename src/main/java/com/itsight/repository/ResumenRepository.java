package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Resumen;

@Repository
public interface ResumenRepository extends JpaRepository<Resumen, Integer>{

	@Override
    @EntityGraph(value="resumen.contenidoWeb", attributePaths = {})
    List<Resumen> findAll();
	
    @EntityGraph(value="resumen", attributePaths = {})
	Resumen findResumenById(Integer resumenId);
    
    @Query("SELECT NEW Resumen(id, titulo,resumen, url, rutaImagenPortada) FROM Resumen R WHERE R.contenidoWeb.id IN ?1 ORDER BY 1 DESC")
    List<Resumen> findAllByIdIn(Integer[] ids);

    @Modifying
    @Query("UPDATE Resumen E SET E.nombreImagenPortada = ?1, E.rutaImagenPortada = ?2 WHERE E.id = ?3")
    void updatingResumenImageById(String nombre, String ruta, int id);
	
}
