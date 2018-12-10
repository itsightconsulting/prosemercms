package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.QuienesSomos;

@Repository
public interface QuienesSomosRepository extends JpaRepository<QuienesSomos, Integer> {
	
	@EntityGraph(value = "quienesSomos", attributePaths = {})
	List<QuienesSomos> findByOrderByIdAsc();
	
	@EntityGraph(value = "quienesSomos", attributePaths = {})
	QuienesSomos findByNombreMenuContaining(String nombreMenu);
	
	@Query("SELECT Q.contenidoWeb.id FROM QuienesSomos Q WHERE Q.id = ?1")
	Integer findContenidoIdById(int quienesSomosId);
}
