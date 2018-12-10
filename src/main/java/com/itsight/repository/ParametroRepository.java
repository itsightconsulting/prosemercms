package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Parametro;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Integer> {
	
	List<Parametro> findAllByClaveContaining(String clave);
	
	Parametro findByClave(String clave);

	List<Parametro> findAllByClaveContainingIgnoreCase(String comodin);
	
}
