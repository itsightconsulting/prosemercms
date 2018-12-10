package com.itsight.repository.en;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.en.EnSlider;

@Repository
public interface EnSliderRepository extends JpaRepository<EnSlider, Integer> {

	@Query("SELECT S FROM EnSlider S WHERE S.flagActivo = ?1 ORDER BY 1 DESC")
	List<EnSlider> findAllByFlagActivo(Boolean flagActivo);

	@EntityGraph(value = "enSlider")
	List<EnSlider> findAllByOrderByIdDesc();
	
	@Modifying
	@Query("UPDATE EnSlider S SET S.flagPrincipal = false WHERE S.id != ?1 ")
	void updateSlidersPrincipal(int sliderId);
	
}
