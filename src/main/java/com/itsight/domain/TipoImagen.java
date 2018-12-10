package com.itsight.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Entity
@Data
public class TipoImagen {

	@Id
	@GeneratedValue(generator = "tipo_imagen_seq")
	@GenericGenerator(
	        name = "tipo_imagen_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "tipo_imagen_seq", value = "tipo_imagen_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "TipoImagenId")
	private int id;
	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false, precision = 5, scale = 1)
	private BigDecimal ancho;
	
	@Column(nullable = false, precision = 5, scale = 1)
	private BigDecimal alto;
	
	public TipoImagen() {
		// TODO Auto-generated constructor stub
	}
	
	public TipoImagen(int id) {
		this.id = id;
	}

	public TipoImagen(String nombre, Double ancho, Double alto) {
		this.nombre = nombre;
		this.ancho = BigDecimal.valueOf(ancho);
		this.alto = BigDecimal.valueOf(alto);
	}
	
}
