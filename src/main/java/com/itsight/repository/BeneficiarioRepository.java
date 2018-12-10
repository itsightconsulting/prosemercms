package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Beneficiario;
import com.itsight.domain.Perfil;

@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Integer> {
	
	List<Perfil> findAllByNombreContaining(String nombres);
}
