package com.itsight.service;

import java.util.Date;
import java.util.List;

import com.itsight.domain.Estudio;

public interface EstudioService {

	List<Estudio> listAll();
	
	List<Estudio> listTopByTipoEstudioIdAndBeneficiario(Integer tipoEstudioId, int beneficiarioId);
	
	List<Estudio> listTopByTituloPrincipalAndBeneficiario(String comodin, int beneficiarioId);
	
	Estudio add(Estudio estudio);
	
	Estudio update(Estudio estudio);
	
	void delete(int estudioId);
	
	Estudio findOneById(int estudioId);
	
	Estudio findOneWithTipoEstudioById(int estudioId);

	Estudio getEstudioByContenidoWebId(int contenidoWebId);
	
	Integer findContenidoIdById(int estudioId);
	
	Integer findTipoEstudioIdByContenidoWebId(int estudioId);
	
	List<Estudio> findAllByFlagActivo(Boolean flagActivo);

	List<Estudio> findAllByTituloPrincipalContainingIgnoreCase(String comodin);
	
	List<Estudio> findWithBeneficiarioByBeneficiarioId(Integer beneficiarioId);
	
	List<Date> findDistinctFechaEstudio();
	
	List<Date> findDistinctFechaEstudioByBeneficiarioId(int beneficiarioId);
	
	List<Integer> findAllIdsByFechaEstudio(Date fechaEstudio);
	
//	List<Integer> findAllIdsByTipoEstudioId(int tipoEstudioId);
//	
//	List<Integer> findAllIdsByBeneficiarioId(int beneficiarioId);
	
	List<Estudio> findAllByBeneficiarioId(Integer beneficiarioId);

	List<Integer> findAllIdsByFechaEstudioAndBeneficiarioId(Date fechaEstudio, Integer beneficiarioId);
	
//	List<Integer> findAllIdsByOrderId();

	List<Estudio> listTopByTituloPrincipalAndBeneficiarioAndTipo(String comodin, int beneficiarioId, int tipoEstudioId);
	
	List<Estudio> findAllByIdsIn(List<Integer> lstEstudioIds);

	List<Integer> findAllIdsByFlagActivo(boolean flag);

	List<Integer> findAllIdsByFlagActivoAndTipoEstudioId(boolean flag, int tipoEstudioId);

	List<Integer> findAllIdsByFlagActivoAndBeneficiarioId(boolean flag, int beneficiarioId);

	List<Estudio> findAllIdsByFlagActivoAndTituloPrincipalContainingIgnoreCase(boolean flag, String comodin);

	Integer findTipoEstudioIdById(int id);

    List<String> findAllDistinctTags();
}
