package com.itsight.domain.en;

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
import com.itsight.domain.TipoImagen;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "enSliderImagen.tipoimagen",
			attributeNodes = {
					@NamedAttributeNode(value = "tipoImagen"),
			}),
	@NamedEntityGraph(name = "enSliderImagen",
		attributeNodes = {
	}),
})
@Data
public class EnSliderImagen {
	
	@Id
	@GeneratedValue(generator = "en_slider_imagen_seq")
	@GenericGenerator(
	        name = "en_slider_imagen_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "en_slider_imagen_seq", value = "en_slider_imagen_seq"),
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
	private EnSlider slider;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TipoImagenId")
	private TipoImagen tipoImagen;
	
	public EnSliderImagen() {}
	
	public void setTipoImagen(TipoImagen tipoImagen) {
		this.tipoImagen = tipoImagen;
	}
	
	public void setTipoImagen(int tipoImagenId) {
		this.tipoImagen = new TipoImagen(tipoImagenId);
	}
	
	public void setSlider(int sliderId) {
		this.slider = new EnSlider(sliderId);
	}
	
	public void setSlider(EnSlider slider) {
		this.slider = slider;
	}

}
