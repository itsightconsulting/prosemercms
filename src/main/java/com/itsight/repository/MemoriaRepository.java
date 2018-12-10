package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Memoria;

@Repository
public interface MemoriaRepository extends JpaRepository<Memoria, Integer> {
	
	@EntityGraph(value = "memoria", attributePaths = {})
	List<Memoria> findByOrderByIdAsc();

	@Query("SELECT nombreArchivo FROM Memoria WHERE id =?1")
	String getNombreArchivoById(int id);

	@Query("SELECT M.contenidoWeb.id FROM Memoria M WHERE id =?1")
	Integer findContenidoIdById(int id);
}
