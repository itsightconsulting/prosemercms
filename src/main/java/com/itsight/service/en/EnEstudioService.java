package com.itsight.service.en;

import java.util.Date;
import java.util.List;

import com.itsight.domain.en.EnEstudio;

public interface EnEstudioService {

	List<EnEstudio> listAll();
	
	List<EnEstudio> listTopByTipoEstudioIdAndBeneficiario(Integer tipoEstudioId, int beneficiarioId);
	
	List<EnEstudio> listTopByTituloPrincipalAndBeneficiario(String comodin, int beneficiarioId);
	
	EnEstudio add(EnEstudio estudio);
	
	EnEstudio update(EnEstudio estudio);
	
	void delete(int estudioId);
	
	EnEstudio findOneById(int estudioId);
	
	EnEstudio findOneWithTipoEstudioById(int estudioId);

	EnEstudio getEstudioByContenidoWebId(int contenidoWebId);
	
	Integer findContenidoIdById(int estudioId);
	
	List<EnEstudio> findAllByFlagActivo(Boolean flagActivo);

	List<EnEstudio> findAllByTituloPrincipalContainingIgnoreCase(String comodin);
	
	List<EnEstudio> findWithBeneficiarioByBeneficiarioId(Integer beneficiarioId);
	
	List<Date> findDistinctFechaEstudio();
	
	List<Date> findDistinctFechaEstudioByBeneficiarioId(int beneficiarioId);
	
	List<Integer> findAllIdsByFechaEstudio(Date fechaEstudio);
	
	List<Integer> findAllIdsByTipoEstudioId(int tipoEstudioId);
	
	List<Integer> findAllIdsByBeneficiarioId(int beneficiarioId);

	List<Integer> findAllIdsByTipoEstudioIdAndTituloPrincipalContainingIgnoreCase(int tipoEstudioId, String comodin);
	
	List<EnEstudio> findAllByBeneficiarioId(Integer beneficiarioId);

	List<Integer> findAllIdsByFechaEstudioAndBeneficiarioId(Date fechaEstudio, Integer beneficiarioId);
	
	List<EnEstudio> findAllIdsByTituloPrincipalContainingIgnoreCase(String comodin);

	List<Integer> findAllIdsByOrderId();

	void updatingPortadaImageById(String nombreImagenPortada, String rutaImagenPortada, Integer id);

	List<EnEstudio> findAllByIdsIn(List<Integer> lstEstudioIds);

	void updateStatusById(boolean flagActivo, int id);

    List<String> findAllDistinctTags();
}
