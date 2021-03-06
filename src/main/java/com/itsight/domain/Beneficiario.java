package com.itsight.domain;

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
public class Beneficiario {

	@Id
	@GeneratedValue(generator="beneficiario_seq")
	@GenericGenerator(
	        name = "beneficiario_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "beneficiario_seq", value = "beneficiario_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "BeneficiarioId")
	private int id;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(nullable = false)
	private String nombreExtenso;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "beneficiario")
	private List<Estudio> lstEstudio;
	
	public Beneficiario() {}
	
	public Beneficiario(int id) {
		this.id = id;
	}
	
	public Beneficiario(String nombre, String nombreExtenso) {
		this.nombre = nombre;
		this.nombreExtenso = nombreExtenso;
	}
}
