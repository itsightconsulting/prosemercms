package com.itsight.service.en;

import java.util.List;

import com.itsight.domain.en.EnLogro;

public interface EnLogroService {

	List<EnLogro> listAll();
	
	EnLogro add(EnLogro logro);
	
	EnLogro update(EnLogro logro);
	
	void delete(int logroId);
	
	EnLogro findOneById(int logroId);
	
	EnLogro findOneWithEstudio(int id);
	
	List<EnLogro> findAllByBeneficiarioId(int beneficiarioId);
	
	List<EnLogro> findAllByEstudioId(int estudioId);
	
	List<EnLogro> findAllByResumenContainingIgnoreCase(String comodin);
	
	List<EnLogro> findAllByFlagActivo(Boolean flagActivo);

	List<Integer> findAllByEstudioIdByBeneficiarioId(int beneficiarioId);

	void updateStatusById(boolean flagActivo, int id);

	void updateLogroMultiple(String[] ids, EnLogro logro);

	void updateTakeOffRelacionEstudio(int id);

	void updateStatusMasive(boolean flagActivo, List<Integer> lstIds);

}
