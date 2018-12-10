package com.itsight.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class TipoEstudio {
	
	@Id
	@GeneratedValue(generator="tipo_estudio_seq")
	@GenericGenerator(
	        name = "tipo_estudio_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "tipo_estudio_seq", value = "tipo_estudio_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "TipoEstudioId")
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nombre;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoEstudio")
	private List<Estudio> lstEstudio = new ArrayList<>();

	public TipoEstudio() {}

	public TipoEstudio(int id) {
		this.id = id;
	}

	public TipoEstudio(String nombre) {
		// TODO Auto-generated constructor stub
		this.nombre = nombre;
	}
	
}
