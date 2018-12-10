package com.itsight.service;

import java.util.List;

import com.itsight.domain.Logro;

public interface LogroService {

	List<Logro> listAll();
	
	Logro add(Logro logro);
	
	Logro update(Logro logro);
	
	void delete(int logroId);
	
	Logro findOneById(int logroId);
	
	Logro findOneWithEstudio(int id);
	
	List<Logro> findAllByBeneficiarioId(int beneficiarioId);
	
	List<Logro> findAllByEstudioId(int estudioId);
	
	List<Logro> findAllByResumenContainingIgnoreCase(String comodin);
	
	List<Logro> findAllByFlagActivo(Boolean flagActivo);

	List<Integer> findAllByEstudioIdByBeneficiarioId(int beneficiarioId);

	void updateLogroMultiple(String[] ids, Logro logro);

	List<Integer> findIdsByEstudioId(int id);

	void updateTakeOffRelacionEstudio(int id);

	void updateStatusMasive(boolean flagActivo, List<Integer> lstIds);

    void eliminarLogroCompartido(int id);
}
