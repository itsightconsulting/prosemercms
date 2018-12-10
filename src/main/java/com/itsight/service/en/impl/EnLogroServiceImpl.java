package com.itsight.service.en.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnLogro;
import com.itsight.repository.en.EnLogroRepository;
import com.itsight.service.en.EnLogroService;

@Service
@Transactional
public class EnLogroServiceImpl implements EnLogroService {
	
	private EnLogroRepository logroRepository;
	
	@Autowired
	public EnLogroServiceImpl(EnLogroRepository logroRepository) {
		this.logroRepository = logroRepository;
	}

	@Override
	public List<EnLogro> listAll() {
		// TODO Auto-generated method stub
		return logroRepository.findAllByFlagCompartidoFalseOrderByFechaLogro();
	}

	@Override
	public List<Integer> findAllByEstudioIdByBeneficiarioId(int beneficiarioId) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByEstudioIdByBeneficiarioId(beneficiarioId);
	}

	@Override
	public EnLogro add(EnLogro logro) {
		// TODO Auto-generated method stub
		return logroRepository.save(logro);
	}

	@Override
	public EnLogro update(EnLogro logro) {
		// TODO Auto-generated method stub
		return logroRepository.saveAndFlush(logro);
	}

	@Override
	public void delete(int logroId) {
		// TODO Auto-generated method stub
		logroRepository.delete(new Integer(logroId));
	}

	@Override
	public EnLogro findOneById(int logroId) {
		// TODO Auto-generated method stub
		return logroRepository.findOne(new Integer(logroId));
	}

	@Override
	public EnLogro findOneWithEstudio(int id) {
		// TODO Auto-generated method stub
		return logroRepository.findById(new Integer(id));
	}

	@Override
	public List<EnLogro> findAllByBeneficiarioId(int beneficiarioId) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByBeneficiarioIdAndFlagCompartidoFalse(beneficiarioId);
	}

	@Override
	public List<EnLogro> findAllByEstudioId(int estudioId) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByEstudioId(estudioId);
	}

	@Override
	public List<EnLogro> findAllByResumenContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByResumenContainingIgnoreCaseAndFlagCompartidoFalseOrderByFechaLogro(comodin);
	}

	@Override
	public List<EnLogro> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByFlagActivoAndFlagCompartidoFalseOrderByFechaLogro(flagActivo);
	}

	@Override
	public void updateStatusById(boolean flagActivo, int id) {
		// TODO Auto-generated method stub
		logroRepository.updateStatusById(flagActivo, id);
	}

	@Override
	public void updateLogroMultiple(String[] ids, EnLogro logro) {
		// TODO Auto-generated method stub
		List<Integer> lstIds = new ArrayList<>();
		for (String x : ids) {
			lstIds.add(Integer.parseInt(x));
		}
		logroRepository.updateLogroMultiple(lstIds, logro.getDescripcion(), logro.getResumen(), logro.getFechaModificacion(), logro.getFechaLogro());
	}
	
	@Override
	public void updateTakeOffRelacionEstudio(int id) {
		// TODO Auto-generated method stub
		logroRepository.updateTakeOffRelacionEstudio(id);
	}

	@Override
	public void updateStatusMasive(boolean flagActivo, List<Integer> lstIds) {
		// TODO Auto-generated method stub
		logroRepository.updateStatusMasive(flagActivo, lstIds);
	}
	
	
}
