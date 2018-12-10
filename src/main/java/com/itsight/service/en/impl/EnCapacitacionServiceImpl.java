package com.itsight.service.en.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnCapacitacion;
import com.itsight.repository.en.EnCapacitacionRepository;
import com.itsight.service.en.EnCapacitacionService;

@Service
@Transactional
public class EnCapacitacionServiceImpl implements EnCapacitacionService {
	
	private EnCapacitacionRepository capacitacionRepository;
	
	@Autowired
	public EnCapacitacionServiceImpl(EnCapacitacionRepository capacitacionRepository) {
		this.capacitacionRepository = capacitacionRepository;
	}

	@Override
	public List<EnCapacitacion> listAll() {
		// TODO Auto-generated method stub
		return capacitacionRepository.findAllByOrderByFechaCapacitacionDescTituloPrincipalAsc();
	}

	@Override
	public List<EnCapacitacion> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return capacitacionRepository.findAllByFlagActivoOrderByFechaCapacitacionDescTituloPrincipalAsc(flagActivo);
	}

	@Override
	public List<EnCapacitacion> findAllByTituloPrincipalContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return capacitacionRepository.findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaCapacitacionDescTituloPrincipalAsc(comodin);
	}

	@Override
	public EnCapacitacion add(EnCapacitacion capacitacion) {
		// TODO Auto-generated method stub
		return capacitacionRepository.save(capacitacion);
	}

	@Override
	public EnCapacitacion update(EnCapacitacion capacitacion) {
		// TODO Auto-generated method stub
		return capacitacionRepository.saveAndFlush(capacitacion);
	}

	@Override
	public void delete(int capacitacionId) {
		// TODO Auto-generated method stub
		capacitacionRepository.delete(new Integer(capacitacionId));
	}

	@Override
	public EnCapacitacion findOneById(int capacitacionId) {
		// TODO Auto-generated method stub
		return capacitacionRepository.findOne(new Integer(capacitacionId));
	}

	@Override
	public EnCapacitacion getCapacitacionByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return capacitacionRepository.findCapacitacionByContenidoWebId(contenidoWebId);
	}

	@Override
	public Integer findContenidoIdById(int id) {
		// TODO Auto-generated method stub
		return capacitacionRepository.findContenidoIdById(id);
	}

	@Override
	public List<Date> findDistinctFechaCapacitacion() throws ParseException {
		// TODO Auto-generated method stub
		List<Date> lstDate = capacitacionRepository.findDistinctFechaCapacitacion();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < lstDate.size(); i++) {			
			String dt = sdf.format(lstDate.get(i))+"-01";
			lstDate.set(i, sdf2.parse(dt));
		}
		Set<Date> lstDateFilter = new HashSet<>();
		lstDateFilter.addAll(lstDate);
		List<Date> filterDates = new ArrayList<>();
		filterDates.addAll(lstDateFilter);
		Collections.sort(filterDates, new Comparator<Date>() {
			public int compare(Date o1, Date o2) {
				return o2.compareTo(o1);
			}
		});
		return filterDates;
	}

	@Override
	public List<Integer> findAllIdsByFechaCapacitacion(Date fechaCapacitacion) {
		// TODO Auto-generated method stub
		String date = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(fechaCapacitacion);
		return capacitacionRepository.findAllIdsByFechaCapacitacion(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5)));
	}

	@Override
	public void updateStatusById(boolean flagActivo, int id) {
		// TODO Auto-generated method stub
		capacitacionRepository.updateStatusById(flagActivo, id);
	}
	
	@Override
	public void updateTakeOffRelacionEstudio(int id) {
		// TODO Auto-generated method stub
		capacitacionRepository.updateTakeOffRelacionEstudio(id);
	}

	@Override
	public List<String> findAllDistinctTags() {
		return capacitacionRepository.findAllDistinctTags();
	}
}
