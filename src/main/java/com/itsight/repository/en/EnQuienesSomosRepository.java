package com.itsight.repository.en;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnQuienesSomos;

@Repository
public interface EnQuienesSomosRepository extends JpaRepository<EnQuienesSomos, Integer> {
	
	@EntityGraph(value = "enQuienesSomos", attributePaths = {})
	List<EnQuienesSomos> findByOrderByIdAsc();
	
	@EntityGraph(value = "enQuienesSomos", attributePaths = {})
	EnQuienesSomos findByNombreMenuContaining(String nombreMenu);
	
	@Query("SELECT Q.contenidoWeb.id FROM EnQuienesSomos Q WHERE Q.id = ?1")
	Integer findContenidoIdById(int quienesSomosId);
}
