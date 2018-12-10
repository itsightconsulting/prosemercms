package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
	
	@Query("SELECT T FROM Tag T ORDER BY T.nombre ASC")
	List<Tag> listAll();
	
	@EntityGraph(value = "tag.tipo")
	List<Tag> findAllByOrderByNombreAsc();
	
	@EntityGraph(value = "tag.tipo")
	List<Tag> findAllByNombreContaining(String nombre);
	
	@EntityGraph(value = "tag.tipo")
	List<Tag> findAllByTipoTagIdOrderByNombreAsc(int tipo);

	@Query("SELECT NEW Tag(nombre) FROM Tag T WHERE T.tipoTag.id = ?1 ORDER BY T.nombre ASC")
	List<Tag> listAllByTipoTag(int tipo);

	@EntityGraph(value = "tag.tipo")
	List<Tag> findAllByNombreContainingIgnoreCaseOrderByNombreAsc(String comodin);

	@EntityGraph(value = "tag")
	Tag findOneByNombreIgnoreCase(String nombre);
	
}
