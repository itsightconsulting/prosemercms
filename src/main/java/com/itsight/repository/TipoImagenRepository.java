package com.itsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsight.domain.TipoImagen;

@Repository
public interface TipoImagenRepository extends JpaRepository<TipoImagen, Integer> {

}
