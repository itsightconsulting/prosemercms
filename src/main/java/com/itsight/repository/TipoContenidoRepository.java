package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.TipoContenido;

@Repository
@Transactional
public interface TipoContenidoRepository extends JpaRepository<TipoContenido, Integer> {
	
	List<TipoContenido> findAllByNombreContaining(String nombre);
	
	TipoContenido findByNombre(String nombre);
	
}
