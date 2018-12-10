package com.itsight.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Logro;

@Repository
public interface LogroRepository extends JpaRepository<Logro, Integer> {
	
	@EntityGraph(value = "logro.estudio")
	Logro findById(int id);
	
	@EntityGraph(value = "logro.beneficiario")
	List<Logro> findAllByFlagCompartidoFalseOrderByFechaLogro();
	
	@Query("SELECT NEW Logro(descripcion) FROM Logro L WHERE L.beneficiario.id = ?1 AND L.flagActivo = true AND L.flagCompartido = false ORDER BY L.fechaLogro ASC")
	List<Logro> findAllByBeneficiarioIdAndFlagCompartidoFalse(Integer beneficiarioId);
	
	@Query("SELECT NEW Logro(descripcion) FROM Logro L WHERE L.estudio.id = ?1 AND L.flagActivo = true ORDER BY L.fechaLogro ASC")
	List<Logro> findAllByEstudioId(Integer estudioId);

	@EntityGraph(value = "logro.beneficiario")
	List<Logro> findAllByResumenContainingIgnoreCaseAndFlagCompartidoFalseOrderByFechaLogro(String comodin);

	@EntityGraph(value = "logro.beneficiario")
	List<Logro> findAllByFlagActivoAndFlagCompartidoFalseOrderByFechaLogro(Boolean flagActivo);

	@Query("SELECT DISTINCT L.estudio.id FROM Logro L WHERE L.beneficiario.id = ?1 AND L.flagActivo = true")
	List<Integer> findAllByEstudioIdByBeneficiarioId(int beneficiarioId);

	@Modifying
	@Query("UPDATE Logro L SET L.descripcion = ?2, L.resumen =?3, L.fechaModificacion =?4, L.fechaLogro =?5 WHERE L.id IN ?1")
	void updateLogroMultiple(List<Integer> ids, String descripcion, String resumen, Date fechaModificacion, Date fechaLogro);

	@Query("SELECT DISTINCT L.id FROM Logro L WHERE L.estudio.id = ?1")
	List<Integer> findIdsByEstudioId(int id);

	@Modifying
	@Query("UPDATE Logro L SET L.estudio.id = null WHERE L.estudio.id = ?1")
	void updateTakeOffRelacionEstudio(int id);

	@Modifying
	@Query("UPDATE Logro L SET L.flagActivo = ?1 WHERE L.id IN ?2")
	void updateStatusMasive(boolean flagActivo, List<Integer> lstIds);

	@Query("SELECT L.multiple FROM Logro L WHERE L.id = ?1")
    String findMultipleById(int id);

	@Modifying
	@Query("DELETE FROM Logro L WHERE L.id IN ?1")
	void deleteInByIds(List<Integer> lstIds);
}
