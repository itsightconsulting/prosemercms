package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itsight.domain.Slider;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Integer> {

	@Query("SELECT S FROM Slider S WHERE S.flagActivo = ?1 ORDER BY 1 DESC")
	List<Slider> findAllByFlagActivo(Boolean flagActivo);

	@EntityGraph(value = "slider")
	List<Slider> findAllByOrderByIdDesc();
	
	@Modifying
	@Query("UPDATE Slider S SET S.flagPrincipal = false WHERE S.id != ?1 ")
	void updateSlidersPrincipal(int sliderId);
	
}
