package com.itsight.service.en.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.en.EnEstudio;
import com.itsight.repository.en.EnEstudioRepository;
import com.itsight.service.en.EnEstudioService;

@Service
@Transactional
public class EnEstudioServiceImpl implements EnEstudioService {
	
	private EnEstudioRepository estudioRepository;
	
	@Autowired
	public EnEstudioServiceImpl(EnEstudioRepository estudioRepository) {
		this.estudioRepository = estudioRepository;
	}

	@Override
	public List<EnEstudio> listAll() {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByOrderByFechaEstudioDescTituloPrincipalAsc();
	}

	@Override
	public List<EnEstudio> findAllByIdsIn(List<Integer> lstEstudioIds) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByIdsIn(lstEstudioIds);
	}

	@Override
	public List<EnEstudio> listTopByTipoEstudioIdAndBeneficiario(Integer tipoEstudioId, int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findTop10ByTipoEstudioIdAndBeneficiarioIdOrderByIdDesc(tipoEstudioId, beneficiarioId, new PageRequest(0, 10));
	}
	
	@Override
	public List<EnEstudio> listTopByTituloPrincipalAndBeneficiario(String tituloPrincipal, int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findTop10ByTituloPrincipalAndBeneficiarioIdOrderByIdDesc(tituloPrincipal, beneficiarioId, new PageRequest(0, 10));
	}

	@Override
	public List<EnEstudio> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByFlagActivoOrderByFechaEstudioDescTituloPrincipalAsc(flagActivo);
	}

	@Override
	public List<EnEstudio> findAllByTituloPrincipalContainingIgnoreCase(String titulo) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEstudioDescTituloPrincipalAsc(titulo);
	}
	
	@Override
	public List<Date> findDistinctFechaEstudio() {
		// TODO Auto-generated method stub
		return estudioRepository.findDistinctFechaEstudio();
	}

	@Override
	public List<Date> findDistinctFechaEstudioByBeneficiarioId(int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findDistinctFechaEstudioByBeneficiarioId(beneficiarioId);
	}

	@Override
	public List<Integer> findAllIdsByFechaEstudio(Date fechaEstudio) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByFechaEstudio(fechaEstudio);
	}
	
	@Override
	public List<Integer> findAllIdsByFechaEstudioAndBeneficiarioId(Date fechaEstudio, Integer beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByFechaEstudioAndBeneficiarioId(fechaEstudio, beneficiarioId);
	}

	@Override
	public List<Integer> findAllIdsByTipoEstudioId(int tipoEstudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByTipoEstudioId(tipoEstudioId);
	}
	
	@Override
	public List<Integer> findAllIdsByBeneficiarioId(int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByBeneficiarioId(beneficiarioId);
	}

	@Override
	public List<Integer> findAllIdsByTipoEstudioIdAndTituloPrincipalContainingIgnoreCase(int tipoEstudioId, String comodin) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByTipoEstudioIdAndTituloPrincipalContainingIgnoreCase(tipoEstudioId, comodin);
	}

	@Override
	public List<EnEstudio> findAllIdsByTituloPrincipalContainingIgnoreCase(String comodin) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByTituloPrincipalContainingIgnoreCase(comodin);
	}

	@Override
	public List<Integer> findAllIdsByOrderId() {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByOrderId();
	}

	@Override
	public List<EnEstudio> findAllByBeneficiarioId(Integer beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByBeneficiarioId(beneficiarioId);
	}

	@Override
	public List<EnEstudio> findWithBeneficiarioByBeneficiarioId(Integer beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findWithBeneficiarioByBeneficiarioId(beneficiarioId);
	}

	@Override
	public EnEstudio add(EnEstudio estudio) {
		// TODO Auto-generated method stub
		return estudioRepository.save(estudio);
	}

	@Override
	public EnEstudio update(EnEstudio estudio) {
		// TODO Auto-generated method stub
		return estudioRepository.saveAndFlush(estudio);
	}

	@Override
	public void delete(int estudioId) {
		// TODO Auto-generated method stub
		estudioRepository.delete(new Integer(estudioId));
	}

	@Override
	public EnEstudio findOneById(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findOne(new Integer(estudioId));
	}

	@Override
	public EnEstudio findOneWithTipoEstudioById(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findOneWithTipoEstudioById(estudioId);
	}

	@Override
	public EnEstudio getEstudioByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return estudioRepository.findEstudioByContenidoWebId(contenidoWebId);
	}

	@Override
	public Integer findContenidoIdById(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findContenidoIdById(estudioId);
	}

	@Override
	public void updatingPortadaImageById(String nombreImagenPortada, String rutaImagenPortada, Integer id) {
		// TODO Auto-generated method stub
		estudioRepository.updatingPortadaImageById(nombreImagenPortada, rutaImagenPortada, id);
	}

	@Override
	public void updateStatusById(boolean flagActivo, int id) {
		// TODO Auto-generated method stub
		estudioRepository.updateStatusById(flagActivo, id);
	}

	@Override
	public List<String> findAllDistinctTags() {
		return estudioRepository.findAllDistinctTags();
	}
}
