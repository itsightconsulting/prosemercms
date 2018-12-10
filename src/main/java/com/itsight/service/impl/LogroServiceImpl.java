package com.itsight.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itsight.repository.en.EnLogroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Logro;
import com.itsight.repository.LogroRepository;
import com.itsight.service.LogroService;

@Service
@Transactional
public class LogroServiceImpl implements LogroService {
	
	private LogroRepository logroRepository;

	private EnLogroRepository enLogroRepository;
	
	@Autowired
	public LogroServiceImpl(LogroRepository logroRepository, EnLogroRepository enLogroRepository) {
		this.logroRepository = logroRepository;
		this.enLogroRepository = enLogroRepository;
	}

	@Override
	public List<Logro> listAll() {
		// TODO Auto-generated method stub
		return logroRepository.findAllByFlagCompartidoFalseOrderByFechaLogro();
	}

	@Override
	public Logro add(Logro logro) {
		// TODO Auto-generated method stub
		return logroRepository.save(logro);
	}

	@Override
	public Logro update(Logro logro) {
		// TODO Auto-generated method stub
		return logroRepository.saveAndFlush(logro);
	}

	@Override
	public void delete(int logroId) {
		// TODO Auto-generated method stub
		logroRepository.delete(new Integer(logroId));
	}

	@Override
	public Logro findOneById(int logroId) {
		// TODO Auto-generated method stub
		return logroRepository.findOne(new Integer(logroId));
	}

	@Override
	public Logro findOneWithEstudio(int id) {
		// TODO Auto-generated method stub
		return logroRepository.findById(new Integer(id));
	}

	@Override
	public List<Logro> findAllByBeneficiarioId(int beneficiarioId) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByBeneficiarioIdAndFlagCompartidoFalse(beneficiarioId);
	}

	@Override
	public List<Logro> findAllByEstudioId(int estudioId) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByEstudioId(estudioId);
	}

	@Override
	public List<Logro> findAllByResumenContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByResumenContainingIgnoreCaseAndFlagCompartidoFalseOrderByFechaLogro(comodin);
	}

	@Override
	public List<Logro> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByFlagActivoAndFlagCompartidoFalseOrderByFechaLogro(flagActivo);
	}

	@Override
	public List<Integer> findAllByEstudioIdByBeneficiarioId(int beneficiarioId) {
		// TODO Auto-generated method stub
		return logroRepository.findAllByEstudioIdByBeneficiarioId(beneficiarioId);
	}

	@Override
	public void updateLogroMultiple(String[] ids, Logro logro) {
		// TODO Auto-generated method stub
		List<Integer> lstIds = new ArrayList<>();
		for (String x : ids) {
			lstIds.add(Integer.parseInt(x));
		}
		logroRepository.updateLogroMultiple(lstIds, logro.getDescripcion(), logro.getResumen(), logro.getFechaModificacion(), logro.getFechaLogro());
	}

	@Override
	public List<Integer> findIdsByEstudioId(int id) {
		// TODO Auto-generated method stub
		return logroRepository.findIdsByEstudioId(id);
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

	@Override
	public void eliminarLogroCompartido(int id) {
		String ids = logroRepository.findMultipleById(id);
		String[] arrIds = ids.split(",");
		List<Integer> lstIds = new ArrayList<>(arrIds.length);
		for(int i=0; i<arrIds.length;i++){
			lstIds.add(Integer.parseInt(arrIds[i]));
		}
		logroRepository.deleteInByIds(lstIds);
		enLogroRepository.deleteInByIds(lstIds);

	}
}
