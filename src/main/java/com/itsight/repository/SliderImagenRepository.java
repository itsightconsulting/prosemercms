package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsight.domain.SliderImagen;

@Repository
public interface SliderImagenRepository extends JpaRepository<SliderImagen, Integer> {

	@Override
    @EntityGraph(value="sliderImagen.tipoimagen", attributePaths = {})
    List<SliderImagen> findAll();
	
    @EntityGraph(value="sliderImagen.tipoimagen", attributePaths = {})
    List<SliderImagen> findBySliderId(int sliderId);
	
}
