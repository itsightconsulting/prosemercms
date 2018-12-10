package com.itsight.repository.en;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnEvento;

@Repository
public interface EnEventoRepository extends JpaRepository<EnEvento, Integer> {
	
	@EntityGraph(value = "enEvento")
	List<EnEvento> findAllByOrderByFechaEventoAscTituloPrincipalAsc();
	
	@EntityGraph(value = "enEvento")
	List<EnEvento> findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEventoAscTituloPrincipalAsc(String tituloPrincipal);
	
	@EntityGraph(value = "enEvento")
	List<EnEvento> findAllByFlagActivoOrderByFechaEventoAscTituloPrincipalAsc(Boolean flagActivo);
	
	@Query("SELECT DISTINCT E.fechaEvento FROM EnEvento E WHERE E.flagActivo=true ORDER BY 1 ASC")
	List<Date> findDistinctFechaEvento();
	
	@Query("SELECT E.contenidoWeb.id FROM EnEvento E WHERE E.flagActivo=true AND E.fechaEvento = ?1")
	List<Integer> findAllIdsByFechaEvento(Date fechaEvento);

	@Query("SELECT NEW EnEvento(tituloLargo, descripcion, fechaEvento) FROM EnEvento E WHERE E.contenidoWeb.id = ?1 AND E.flagActivo = true")
	EnEvento findEventoByContenidoWebId(int contenidoWebId);

	@Query("SELECT NEW EnEvento(id, tituloPrincipal, tituloLargo, descripcion, tags, fechaEvento, contenidoWeb.id) FROM EnEvento E WHERE E.id = ?1")
	EnEvento findOneWithContenidoWebById(int eventoId);

	@Query("SELECT E.contenidoWeb.id FROM EnEvento E WHERE E.id = ?1")
	Integer findContenidoIdById(int eventoId);

	@Modifying
    @Query("UPDATE EnEvento E SET E.flagActivo = ?1 WHERE E.id = ?2")
	void updateStatusById(boolean flagActivo, int id);

	@Query("SELECT DISTINCT E.tags FROM EnEvento E WHERE E.flagActivo = true")
    List<String> findAllDistinctTags();
}
