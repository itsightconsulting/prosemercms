package com.itsight.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "sliderImagen.tipoimagen",
			attributeNodes = {
					@NamedAttributeNode(value = "tipoImagen"),
			}),
	@NamedEntityGraph(name = "sliderImagen",
		attributeNodes = {
	}),
})
@Data
public class SliderImagen {
	
	@Id
	@GeneratedValue(generator = "slider_imagen_seq")
	@GenericGenerator(
	        name = "slider_imagen_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "slider_imagen_seq", value = "slider_imagen_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "SliderImagenId")
	private int id;
	
	@Column(nullable = false)
	private String nombreMedia;
	
	@Column(nullable = false)
	private String rutaMedia;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SliderId")
	private Slider slider;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TipoImagenId")
	private TipoImagen tipoImagen;
	
	public void setTipoImagen(TipoImagen tipoImagen) {
		this.tipoImagen = tipoImagen;
	}
	
	public void setTipoImagen(int tipoImagenId) {
		this.tipoImagen = new TipoImagen(tipoImagenId);
	}
	
	public void setSlider(int sliderId) {
		this.slider = new Slider(sliderId);
	}
	
	public void setSlider(Slider slider) {
		this.slider = slider;
	}

}
