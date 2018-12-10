package com.itsight.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@NamedEntityGraphs(value = {
		@NamedEntityGraph(name = "slider",
				attributeNodes = {}),
		@NamedEntityGraph(name = "slider.lstImages",
				attributeNodes = {
						@NamedAttributeNode(value = "lstSliderImagen"),
		}),
})
@Data
public class Slider {
	
	@Id
	@GeneratedValue(generator = "slider_seq")
	@GenericGenerator(
	        name = "slider_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "slider_seq", value = "slider_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "SliderId")
	private int id;
	
	@Column(nullable = true)
	private String hyperVinculo;
	
	@Column(nullable = true)
	private String rutaImagenWeb;
	
	@Column(nullable = true)
	private String rutaImagenMobile;
	
	@Column(nullable = false)
	private Boolean flagPrincipal;
	
	@Column(nullable = false)
	private boolean flagActivo;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "slider", cascade = {CascadeType.REMOVE})
	private List<SliderImagen> lstSliderImagen;
	
	public Slider() {}

	public Slider(int id) {
		this.id = id;
	}

	public Slider(int id, String hyperVinculo) {
		this.id = id;
		this.hyperVinculo = hyperVinculo;
	}
	
	public Slider(String hyperVinculo, boolean flagActivo) {
		this.hyperVinculo = hyperVinculo;
		this.flagActivo = flagActivo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return hyperVinculo;
	}

	public void setNombre(String hyperVinculo) {
		this.hyperVinculo = hyperVinculo;
	}

	public boolean isFlagActivo() {
		return flagActivo;
	}

	public void setFlagActivo(boolean flagActivo) {
		this.flagActivo = flagActivo;
	}

	@Override
	public String toString() {
		return "Slider [id=" + id + ", hyperVinculo=" + hyperVinculo + ", flagActivo=" + flagActivo + "]";
	}

}
