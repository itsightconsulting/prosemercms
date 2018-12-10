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
public class Perfil {

	@Id
	@GeneratedValue(generator="perfil_seq")
	@GenericGenerator(
	        name = "perfil_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "perfil_seq", value = "perfil_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "PerfilId")
	private int id;
	
	@Column(nullable = false)
	private String nombre;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "perfil")
	private List<Usuario> lstUsuario;
	
	public Perfil() {}

	public Perfil(int id) {
		this.id = id;
	}

	public Perfil(String nombre) {
		this.nombre = nombre;
	}
}
