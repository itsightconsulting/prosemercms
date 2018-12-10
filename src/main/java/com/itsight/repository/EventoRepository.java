package com.itsight.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
	
	@EntityGraph(value = "evento")
	List<Evento> findAllByOrderByFechaEventoAscTituloPrincipalAsc();
	
	@EntityGraph(value = "evento")
	List<Evento> findAllByTituloPrincipalContainingIgnoreCaseOrderByFechaEventoAscTituloPrincipalAsc(String tituloPrincipal);
	
	@EntityGraph(value = "evento")
	List<Evento> findAllByFlagActivoOrderByFechaEventoAscTituloPrincipalAsc(Boolean flagActivo);
	
	@Query("SELECT DISTINCT E.fechaEvento FROM Evento E WHERE E.flagActivo=true ORDER BY 1 ASC")
	List<Date> findDistinctFechaEvento();
	
	@Query("SELECT E.contenidoWeb.id FROM Evento E WHERE E.flagActivo=true AND E.fechaEvento = ?1")
	List<Integer> findAllIdsByFechaEvento(Date fechaEvento);

	@Query("SELECT NEW Evento(tituloLargo, descripcion, fechaEvento) FROM Evento E WHERE E.contenidoWeb.id = ?1 AND E.flagActivo = true")
	Evento findEventoByContenidoWebId(int contenidoWebId);

	@Query("SELECT NEW Evento(id, tituloPrincipal, tituloLargo, descripcion, tags, fechaEvento, contenidoWeb.id) FROM Evento E WHERE E.id = ?1")
	Evento findOneWithContenidoWebById(int eventoId);

	@Query("SELECT E.contenidoWeb.id FROM Evento E WHERE E.id = ?1")
	Integer findContenidoIdById(int eventoId);

	@Query("SELECT DISTINCT E.tags FROM Evento E WHERE E.flagActivo = true")
    List<String> findAllDistinctTags();
}
