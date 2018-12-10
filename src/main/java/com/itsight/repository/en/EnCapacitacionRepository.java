package com.itsight.repository.en;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnCapacitacion;

@Repository
public interface EnCapacitacionRepository extends JpaRepository<EnCapacitacion, Integer> {
	
	@Override
	@EntityGraph(value = "enCapacitacion.estudio", attributePaths = {})
	EnCapacitacion findOne(Integer id);
	
	@EntityGraph(value = "enCapacitacion.beneficiario", attributePaths = {})
	List<EnCapacitacion> findAllByOrderByFechaCapacitacionDescTituloPrincipalAsc();
		
	@EntityGraph(value = "enCapacitacion.beneficiario", attributePaths = {})
	List<EnCapacitacion> findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaCapacitacionDescTituloPrincipalAsc(String tituloPrincipal);
	
	@EntityGraph(value = "enCapacitacion.beneficiario", attributePaths = {})
	List<EnCapacitacion> findAllByFlagActivoOrderByFechaCapacitacionDescTituloPrincipalAsc(Boolean flagActivo);

	@Query("SELECT NEW EnCapacitacion(tituloPrincipal, tituloLargo, tituloEstudioReferencia, descripcion) FROM EnCapacitacion C WHERE C.contenidoWeb.id = ?1 AND C.flagActivo= true")
	EnCapacitacion findCapacitacionByContenidoWebId(Integer contenidoWebId);
	
	@Query("SELECT C.contenidoWeb.id FROM EnCapacitacion C WHERE C.id = ?1")
	Integer findContenidoIdById(int id);
	
	@Query("SELECT DISTINCT C.fechaCapacitacion FROM EnCapacitacion C WHERE C.flagActivo = true ORDER BY 1 DESC")
	List<Date> findDistinctFechaCapacitacion();
	
	@Query("SELECT C.contenidoWeb.id FROM EnCapacitacion C WHERE YEAR(C.fechaCapacitacion) = ?1 and MONTH(C.fechaCapacitacion) = ?2 AND C.flagActivo = true ORDER BY 1 ASC")
	List<Integer> findAllIdsByFechaCapacitacion(int year, int month);

	@Modifying
    @Query("UPDATE EnCapacitacion E SET E.flagActivo = ?1 WHERE E.id = ?2")
	void updateStatusById(boolean flagActivo, int id);

	@Modifying
	@Query("UPDATE EnCapacitacion C SET C.estudio.id = null WHERE C.estudio.id = ?1")
	void updateTakeOffRelacionEstudio(int id);

	@Query("SELECT DISTINCT C.tags FROM EnCapacitacion C WHERE C.flagActivo = true")
    List<String> findAllDistinctTags();
}
