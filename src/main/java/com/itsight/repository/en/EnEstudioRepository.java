package com.itsight.repository.en;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnEstudio;

@Repository
public interface EnEstudioRepository extends JpaRepository<EnEstudio, Integer> {
	
	@EntityGraph(value = "enEstudio.beneficiario")
	List<EnEstudio> findAllByOrderByFechaEstudioDescTituloPrincipalAsc();

	@EntityGraph(value = "enEstudio.all")
	EnEstudio findOneWithTipoEstudioById(int estudioId);
	
	@EntityGraph(value = "enEstudio.beneficiario")
	List<EnEstudio> findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEstudioDescTituloPrincipalAsc(String tituloPrincipal);
	
	@EntityGraph(value = "enEstudio.beneficiario")
	List<EnEstudio> findAllByFlagActivoOrderByFechaEstudioDescTituloPrincipalAsc(Boolean flagActivo);
	
	@EntityGraph(value = "enEstudio.beneficiario")
	List<EnEstudio> findWithBeneficiarioByBeneficiarioId(Integer beneficiarioId);

	@Query("SELECT NEW EnEstudio(id, tituloPrincipal, beneficiario.id) FROM EnEstudio E WHERE E.tipoEstudio.id = ?1 AND E.beneficiario.id = ?2 ORDER BY 1 DESC")
	List<EnEstudio> findTop10ByTipoEstudioIdAndBeneficiarioIdOrderByIdDesc(Integer tipoEstudioId, Integer beneficiarioId, Pageable page);

	@Query("SELECT NEW EnEstudio(id, tituloPrincipal, beneficiario.id) FROM EnEstudio E WHERE LOWER(E.tituloPrincipal)  LIKE LOWER(concat('%', ?1, '%')) AND E.beneficiario.id = ?2 ORDER BY 1 DESC")
	List<EnEstudio> findTop10ByTituloPrincipalAndBeneficiarioIdOrderByIdDesc(String tituloPrincipal, Integer beneficiarioId, Pageable page);
	
	@Query("SELECT E,B FROM EnEstudio E INNER JOIN E.beneficiario B WHERE E.contenidoWeb.id = ?1 AND E.flagActivo= true")
	EnEstudio findEstudioByContenidoWebId(Integer contenidoWebId);
	
	@Query("SELECT DISTINCT E.fechaEstudio FROM EnEstudio E WHERE E.flagActivo= true ORDER BY 1 DESC")
	List<Date> findDistinctFechaEstudio();
	
	@Query("SELECT DISTINCT E.fechaEstudio FROM EnEstudio E WHERE E.flagActivo = true AND E.beneficiario.id = ?1 ORDER BY 1 DESC")
	List<Date> findDistinctFechaEstudioByBeneficiarioId(int beneficiarioId);
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E ORDER BY 1 ASC")
	List<Integer> findAllIdsByOrderId();
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E WHERE E.fechaEstudio = ?1 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFechaEstudio(Date fechaEstudio);
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E WHERE E.fechaEstudio = ?1 AND E.beneficiario.id = ?2 ORDER BY 1 ASC")
	List<Integer> findAllIdsByFechaEstudioAndBeneficiarioId(Date fechaEstudio, Integer beneficiarioId);
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E WHERE E.tipoEstudio.id = ?1 ORDER BY 1 ASC")
	List<Integer> findAllIdsByTipoEstudioId(int tipoEstudioId);
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E WHERE E.beneficiario.id = ?1 ORDER BY 1 ASC")
	List<Integer> findAllIdsByBeneficiarioId(int beneficiarioId);
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E WHERE E.tipoEstudio.id = ?1 AND LOWER(E.tituloPrincipal) LIKE LOWER(concat('%', ?2, '%')) ORDER BY 1 ASC")
	List<Integer> findAllIdsByTipoEstudioIdAndTituloPrincipalContainingIgnoreCase(int tipoEstudioId, String comodin);
	
	@Query("SELECT NEW EnEstudio(E.contenidoWeb.id,E.tipoEstudio.id,E.beneficiario.id) FROM EnEstudio E WHERE LOWER(E.tituloPrincipal) LIKE LOWER(concat('%', ?1, '%')) ORDER BY 1 ASC")
	List<EnEstudio> findAllIdsByTituloPrincipalContainingIgnoreCase(String comodin);
					
	@Query("SELECT NEW EnEstudio(id, tituloPrincipal) FROM EnEstudio E WHERE E.beneficiario.id = ?1 ORDER BY 1 DESC")
	List<EnEstudio> findAllByBeneficiarioId(Integer beneficiarioId);
	
	@Query("SELECT NEW EnEstudio(id, tituloPrincipal) FROM EnEstudio E WHERE E.id IN ?1 ORDER BY E.fechaEstudio DESC")
	List<EnEstudio> findAllByIdsIn(List<Integer> lstEstudioIds);
	
	@Query("SELECT E.contenidoWeb.id FROM EnEstudio E WHERE E.id = ?1")
	Integer findContenidoIdById(int estudioId);
	
	@Modifying
    @Query("UPDATE EnEstudio E SET E.nombreImagenPortada = ?1, E.rutaImagenPortada = ?2 WHERE E.id = ?3")
	void updatingPortadaImageById(String nombreImagenPortada, String rutaImagenPortada, Integer id);

	@Modifying
    @Query("UPDATE EnEstudio E SET E.flagActivo = ?1 WHERE E.id = ?2")
	void updateStatusById(boolean flagActivo, int id);

	@Query("SELECT DISTINCT E.tags FROM EnEstudio E WHERE E.flagActivo = true")
    List<String> findAllDistinctTags();
}
