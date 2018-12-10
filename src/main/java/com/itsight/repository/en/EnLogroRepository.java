package com.itsight.repository.en;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnLogro;

@Repository
public interface EnLogroRepository extends JpaRepository<EnLogro, Integer> {
	
	@EntityGraph(value = "enLogro.estudio")
	EnLogro findById(int id);
	
	@EntityGraph(value = "enLogro.beneficiario")
	List<EnLogro> findAllByFlagCompartidoFalseOrderByFechaLogro();
	
	@Query("SELECT NEW EnLogro(descripcion) FROM EnLogro L WHERE L.beneficiario.id = ?1 AND L.flagActivo = true AND L.flagCompartido = false ORDER BY L.fechaLogro ASC")
	List<EnLogro> findAllByBeneficiarioIdAndFlagCompartidoFalse(Integer beneficiarioId);
	
	@Query("SELECT NEW EnLogro(descripcion) FROM EnLogro L WHERE L.estudio.id = ?1 AND L.flagActivo = true ORDER BY L.fechaLogro ASC")
	List<EnLogro> findAllByEstudioId(Integer estudioId);

	@EntityGraph(value = "enLogro.beneficiario")
	List<EnLogro> findAllByResumenContainingIgnoreCaseAndFlagCompartidoFalseOrderByFechaLogro(String comodin);

	@EntityGraph(value = "enLogro.beneficiario")
	List<EnLogro> findAllByFlagActivoAndFlagCompartidoFalseOrderByFechaLogro(Boolean flagActivo);

	@Query("SELECT DISTINCT L.estudio.id FROM EnLogro L WHERE L.beneficiario.id = ?1 AND L.flagActivo = true")
	List<Integer> findAllByEstudioIdByBeneficiarioId(int beneficiarioId);

	@Modifying
    @Query("UPDATE EnLogro L SET L.flagActivo = ?1 WHERE L.id = ?2")
	void updateStatusById(boolean flagActivo, int id);

	@Modifying
	@Query("UPDATE EnLogro L SET L.descripcion = ?2, L.resumen =?3, L.fechaModificacion =?4, L.fechaLogro =?5 WHERE L.id IN ?1")
	void updateLogroMultiple(List<Integer> ids, String descripcion, String resumen, Date fechaModificacion, Date fechaLogro);

	@Modifying
	@Query("UPDATE EnLogro L SET L.estudio.id = null WHERE L.estudio.id = ?1")
	void updateTakeOffRelacionEstudio(int id);

	@Modifying
	@Query("UPDATE EnLogro L SET L.flagActivo = ?1 WHERE L.id IN ?2")
	void updateStatusMasive(boolean flagActivo, List<Integer> lstIds);

	@Modifying
	@Query("DELETE FROM EnLogro L WHERE L.id IN ?1")
	void deleteInByIds(List<Integer> lstIds);
}
