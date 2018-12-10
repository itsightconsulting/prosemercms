package com.itsight.repository.en;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnSliderImagen;

@Repository
public interface EnSliderImagenRepository extends JpaRepository<EnSliderImagen, Integer> {

	@Override
    @EntityGraph(value="enSliderImagen.tipoimagen", attributePaths = {})
    List<EnSliderImagen> findAll();
	
    @EntityGraph(value="enSliderImagen.tipoimagen", attributePaths = {})
    List<EnSliderImagen> findBySliderId(int sliderId);
	
}
