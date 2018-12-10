package com.itsight.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Capacitacion;

@Repository
public interface CapacitacionRepository extends JpaRepository<Capacitacion, Integer> {
	
	@Override
	@EntityGraph(value = "capacitacion.estudio")
	Capacitacion findOne(Integer id);
	
	@EntityGraph(value = "capacitacion.beneficiario")
	List<Capacitacion> findAllByOrderByFechaCapacitacionDescTituloPrincipalAsc();
		
	@EntityGraph(value = "capacitacion.beneficiario")
	List<Capacitacion> findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaCapacitacionDescTituloPrincipalAsc(String tituloPrincipal);
	
	@EntityGraph(value = "capacitacion.beneficiario")
	List<Capacitacion> findAllByFlagActivoOrderByFechaCapacitacionDescTituloPrincipalAsc(Boolean flagActivo);

	@Query("SELECT NEW Capacitacion(tituloPrincipal, tituloLargo, tituloEstudioReferencia, descripcion) FROM Capacitacion C WHERE C.contenidoWeb.id = ?1 AND C.flagActivo= true")
	Capacitacion findCapacitacionByContenidoWebId(Integer contenidoWebId);
	
	@Query("SELECT C.contenidoWeb.id FROM Capacitacion C WHERE C.id = ?1")
	Integer findContenidoIdById(int id);
	
	@Query("SELECT DISTINCT C.fechaCapacitacion FROM Capacitacion C WHERE C.flagActivo = true ORDER BY 1 DESC")
	List<Date> findDistinctFechaCapacitacion();
	
	@Query("SELECT C.contenidoWeb.id FROM Capacitacion C WHERE YEAR(C.fechaCapacitacion) = ?1 and MONTH(C.fechaCapacitacion) = ?2 AND C.flagActivo = true ORDER BY 1 ASC")
	List<Integer> findAllIdsByFechaCapacitacion(int year, int month);

	@Query("SELECT DISTINCT C.id FROM Capacitacion C WHERE C.estudio.id = ?1")
	List<Integer> findIdsByEstudioId(int id);

	@Modifying
	@Query("UPDATE Capacitacion C SET C.estudio.id = null WHERE C.estudio.id = ?1")
	void updateTakeOffRelacionEstudio(int id);

	@Query("SELECT DISTINCT C.tags FROM Capacitacion C WHERE C.flagActivo = true")
    List<String> findAllDistinctTags();

}
