package com.itsight.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Estudio;

@Repository
public interface EstudioRepository extends JpaRepository<Estudio, Integer> {
	
	@EntityGraph(value = "estudio.beneficiario")
	List<Estudio> findAllByOrderByFechaEstudioDescTituloPrincipalAsc();
	
	@EntityGraph(value = "estudio.all")
	Estudio findOneWithTipoEstudioById(int estudioId);
	
	@EntityGraph(value = "estudio.beneficiario")
	List<Estudio> findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEstudioDescTituloPrincipalAsc(String tituloPrincipal);
	
	@EntityGraph(value = "estudio.beneficiario")
	List<Estudio> findAllByFlagActivoOrderByFechaEstudioDescTituloPrincipalAsc(Boolean flagActivo);
	
	@EntityGraph(value = "estudio.beneficiario")
	List<Estudio> findWithBeneficiarioByBeneficiarioId(Integer beneficiarioId);

	@Query("SELECT NEW Estudio(id, tituloPrincipal, beneficiario.id) FROM Estudio E WHERE E.tipoEstudio.id = ?1 AND E.beneficiario.id = ?2 ORDER BY 1 DESC")
	List<Estudio> findTop10ByTipoEstudioIdAndBeneficiarioIdOrderByIdDesc(Integer tipoEstudioId, Integer beneficiarioId);
	
	@Query("SELECT NEW Estudio(id, tituloPrincipal, beneficiario.id) FROM Estudio E WHERE LOWER(E.tituloPrincipal)  LIKE LOWER(concat('%', ?1, '%')) AND E.beneficiario.id = ?2 AND E.tipoEstudio.id = ?3  ORDER BY 1 DESC")
	List<Estudio> findTop10ByTituloPrincipalAndBeneficiarioIdAndTipoIdOrderByIdDesc(String comodin, int beneficiarioId,
			int tipoEstudioId);

	@Query("SELECT NEW Estudio(id, tituloPrincipal, beneficiario.id) FROM Estudio E WHERE LOWER(E.tituloPrincipal)  LIKE LOWER(concat('%', ?1, '%')) AND E.beneficiario.id = ?2 ORDER BY 1 DESC")
	List<Estudio> findTop10ByTituloPrincipalAndBeneficiarioIdOrderByIdDesc(String tituloPrincipal, Integer beneficiarioId);
	
	@Query("SELECT E,B FROM Estudio E INNER JOIN E.beneficiario B WHERE E.contenidoWeb.id = ?1 AND E.flagActivo= true")
	Estudio findEstudioByContenidoWebId(Integer contenidoWebId);
	
	@Query("SELECT DISTINCT E.fechaEstudio FROM Estudio E WHERE E.flagActivo= true ORDER BY 1 DESC")
	List<Date> findDistinctFechaEstudio();
	
	@Query("SELECT DISTINCT E.fechaEstudio FROM Estudio E WHERE E.flagActivo= true AND E.beneficiario.id = ?1 ORDER BY 1 DESC")
	List<Date> findDistinctFechaEstudioByBeneficiarioId(int beneficiarioId);
	
//	@Query("SELECT E.contenidoWeb.id FROM Estudio E ORDER BY 1 ASC")
//	List<Integer> findAllIdsByOrderId();
	
	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.fechaEstudio = ?1 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFechaEstudio(Date fechaEstudio);
	
	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.fechaEstudio = ?1 AND E.beneficiario.id = ?2 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFechaEstudioAndBeneficiarioId(Date fechaEstudio, Integer beneficiarioId);
	
//	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.tipoEstudio.id = ?1 ORDER BY 1 ASC")
//	List<Integer> findAllIdsByTipoEstudioId(int tipoEstudioId);
//	
//	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.beneficiario.id = ?1 ORDER BY 1 ASC")
//	List<Integer> findAllIdsByBeneficiarioId(int beneficiarioId);
	
//	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.tipoEstudio.id = ?1 AND LOWER(E.tituloPrincipal) LIKE LOWER(concat('%', ?2, '%')) ORDER BY 1 ASC")
//	List<Integer> findAllIdsByTipoEstudioIdAndTituloPrincipalContainingIgnoreCase(int tipoEstudioId, String comodin);
	
//	@Query("SELECT NEW Estudio(E.contenidoWeb.id,E.tipoEstudio.id,E.beneficiario.id) FROM Estudio E WHERE LOWER(E.tituloPrincipal) LIKE LOWER(concat('%', ?1, '%')) ORDER BY 1 ASC")
//	List<Estudio> findAllIdsByTituloPrincipalContainingIgnoreCase(String comodin);
					
	@Query("SELECT NEW Estudio(id, tituloPrincipal) FROM Estudio E WHERE E.beneficiario.id = ?1 ORDER BY 1 DESC")
	List<Estudio> findAllByBeneficiarioId(Integer beneficiarioId);
	
	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.id = ?1")
	Integer findContenidoIdById(int estudioId);
	
	@Query("SELECT E.tipoEstudio.id FROM Estudio E WHERE E.contenidoWeb.id = ?1")
	Integer findTipoEstudioIdByContenidoWebId(int estudioId);
	
	@Query("SELECT NEW Estudio(id, tituloPrincipal) FROM Estudio E WHERE E.id IN ?1 ORDER BY E.fechaEstudio DESC")
	List<Estudio> findAllByIdsIn(List<Integer> lstEstudioIds);

	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.flagActivo = ?1 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFlagActivo(boolean flag);

	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.flagActivo = ?1 AND E.tipoEstudio.id = ?2 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFlagActivoAndTipoEstudioId(boolean flag, int tipoEstudioId);

	@Query("SELECT E.contenidoWeb.id FROM Estudio E WHERE E.flagActivo = ?1 AND E.beneficiario.id = ?2 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFlagActivoAndBeneficiarioId(boolean flag, int beneficiarioId);

	@Query("SELECT NEW Estudio(E.contenidoWeb.id,E.tipoEstudio.id,E.beneficiario.id) FROM Estudio E WHERE E.flagActivo = ?1 AND LOWER(E.tituloPrincipal) LIKE LOWER(concat('%', ?2, '%')) ORDER BY 1 ASC")
	List<Estudio> findAllIdsByFlagActivoAndTituloPrincipalContainingIgnoreCase(boolean flag, String comodin);

	@Query("SELECT E.tipoEstudio.id FROM Estudio E WHERE E.id = ?1")
	Integer findTipoEstudioIdById(int id);

	@Query("SELECT DISTINCT E.tags FROM Estudio E WHERE E.flagActivo = true")
    List<String> findAllDistinctTags();
}
