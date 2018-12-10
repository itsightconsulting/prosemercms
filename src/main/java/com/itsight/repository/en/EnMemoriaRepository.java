package com.itsight.repository.en;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnMemoria;

@Repository
public interface EnMemoriaRepository extends JpaRepository<EnMemoria, Integer> {
	
	@EntityGraph(value = "enMemoria", attributePaths = {})
	List<EnMemoria> findByOrderByIdAsc();

	@Query("SELECT nombreArchivo FROM EnMemoria WHERE id =?1")
	String getNombreArchivoById(int id);
	
	@Modifying
    @Query("UPDATE EnMemoria E SET E.nombreImagenPortada = ?1, E.rutaImagenPortada = ?2 WHERE E.id = ?3")
	void updatingPortadaImageById(String nombreImagenPortada, String rutaImagenPortada, int id);
}
