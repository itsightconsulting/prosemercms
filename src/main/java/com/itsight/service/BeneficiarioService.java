package com.itsight.service;

import java.util.List;

import com.itsight.domain.Beneficiario;

public interface BeneficiarioService {

	List<Beneficiario> listAll();
	
	Beneficiario add(Beneficiario beneficiario);
	
	Beneficiario update(Beneficiario beneficiario);
	
	void delete(int beneficiarioId);
	
	Beneficiario findOneById(int beneficiarioId);
	
	
}
