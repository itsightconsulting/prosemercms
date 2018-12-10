package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsight.domain.TipoEstudio;
import com.itsight.domain.Perfil;

@Repository
public interface TipoEstudioRepository extends JpaRepository<TipoEstudio, Integer> {
	
	List<Perfil> findAllByNombreContaining(String nombre);
}
