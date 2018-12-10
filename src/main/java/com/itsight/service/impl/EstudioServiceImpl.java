package com.itsight.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.Estudio;
import com.itsight.repository.EstudioRepository;
import com.itsight.service.EstudioService;

@Service
@Transactional
public class EstudioServiceImpl implements EstudioService {
	
	private EstudioRepository estudioRepository;
	
	@Autowired
	public EstudioServiceImpl(EstudioRepository estudioRepository) {
		this.estudioRepository = estudioRepository;
	}

	@Override
	public List<Estudio> listAll() {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByOrderByFechaEstudioDescTituloPrincipalAsc();
	}

	@Override
	public List<Estudio> findAllByIdsIn(List<Integer> lstEstudioIds) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByIdsIn(lstEstudioIds);
	}

	@Override
	public List<Estudio> listTopByTipoEstudioIdAndBeneficiario(Integer tipoEstudioId, int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findTop10ByTipoEstudioIdAndBeneficiarioIdOrderByIdDesc(tipoEstudioId, beneficiarioId);
	}
	
	@Override
	public List<Estudio> listTopByTituloPrincipalAndBeneficiario(String tituloPrincipal, int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findTop10ByTituloPrincipalAndBeneficiarioIdOrderByIdDesc(tituloPrincipal, beneficiarioId);
	}
	
	@Override
	public List<Estudio> listTopByTituloPrincipalAndBeneficiarioAndTipo(String comodin, int beneficiarioId, int tipoEstudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findTop10ByTituloPrincipalAndBeneficiarioIdAndTipoIdOrderByIdDesc(comodin, beneficiarioId, tipoEstudioId);
	}

	@Override
	public List<Estudio> findAllByFlagActivo(Boolean flagActivo) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByFlagActivoOrderByFechaEstudioDescTituloPrincipalAsc(flagActivo);
	}

	@Override
	public List<Estudio> findAllByTituloPrincipalContainingIgnoreCase(String titulo) {
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
	public List<Estudio> findAllByBeneficiarioId(Integer beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllByBeneficiarioId(beneficiarioId);
	}

	@Override
	public List<Estudio> findWithBeneficiarioByBeneficiarioId(Integer beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findWithBeneficiarioByBeneficiarioId(beneficiarioId);
	}

	@Override
	public Estudio add(Estudio estudio) {
		// TODO Auto-generated method stub
		return estudioRepository.save(estudio);
	}

	@Override
	public Estudio update(Estudio estudio) {
		// TODO Auto-generated method stub
		return estudioRepository.saveAndFlush(estudio);
	}

	@Override
	public void delete(int estudioId) {
		// TODO Auto-generated method stub
		estudioRepository.delete(new Integer(estudioId));
	}

	@Override
	public Estudio findOneById(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findOne(new Integer(estudioId));
	}

	@Override
	public Estudio findOneWithTipoEstudioById(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findOneWithTipoEstudioById(estudioId);
	}

	@Override
	public Estudio getEstudioByContenidoWebId(int contenidoWebId) {
		// TODO Auto-generated method stub
		return estudioRepository.findEstudioByContenidoWebId(contenidoWebId);
	}

	@Override
	public Integer findContenidoIdById(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findContenidoIdById(estudioId);
	}

	@Override
	public Integer findTipoEstudioIdById(int id) {
		// TODO Auto-generated method stub
		return estudioRepository.findTipoEstudioIdById(id);
	}

	@Override
	public Integer findTipoEstudioIdByContenidoWebId(int estudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findTipoEstudioIdByContenidoWebId(estudioId);
	}

	@Override
	public List<Integer> findAllIdsByFlagActivo(boolean flag) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByFlagActivo(flag);
	}

	@Override
	public List<Integer> findAllIdsByFlagActivoAndTipoEstudioId(boolean flag, int tipoEstudioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByFlagActivoAndTipoEstudioId(flag, tipoEstudioId);
	}

	@Override
	public List<Integer> findAllIdsByFlagActivoAndBeneficiarioId(boolean flag, int beneficiarioId) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByFlagActivoAndBeneficiarioId(flag, beneficiarioId);
	}

	@Override
	public List<Estudio> findAllIdsByFlagActivoAndTituloPrincipalContainingIgnoreCase(boolean flag, String comodin) {
		// TODO Auto-generated method stub
		return estudioRepository.findAllIdsByFlagActivoAndTituloPrincipalContainingIgnoreCase(flag, comodin);
	}

	@Override
	public List<String> findAllDistinctTags() {
		return estudioRepository.findAllDistinctTags();
	}
}
