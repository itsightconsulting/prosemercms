package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.free.Archivo;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Integer> {

	@Override
    @EntityGraph(value="archivo", attributePaths = {})
    List<Archivo> findAll();
    
    @Query("SELECT new Archivo(alias, rutaMediaWeb, peso) FROM Archivo A WHERE A.id = ?1 ORDER BY A.id")
    List<Archivo> findById(int id);
    
    @Query("SELECT A.alias FROM Archivo A WHERE A.id = ?1 AND A.uuid = ?2")
	String findAliasByIdAndUuid(Integer id, String uuid);
    
    @Query("SELECT new Archivo(id, nombreMedia, rutaMediaWeb, peso) FROM Archivo A WHERE A.id = ?1 ORDER BY 1")
    List<Archivo> findAllById(int id);
}
