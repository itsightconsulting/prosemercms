package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.TipoTag;

@Repository
@Transactional
public interface TipoTagRepository extends JpaRepository<TipoTag, Integer> {
	
	@Override
	@Query("SELECT T FROM TipoTag T ORDER BY T.nombre ASC")
	List<TipoTag> findAll();
	
	List<TipoTag> findAllByNombreContaining(String nombre);
	
}
