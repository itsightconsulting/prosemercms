package com.itsight.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Entity
@Data
public class TipoTag {
	
	@Id
	@GeneratedValue(generator="tp_tag_seq")
	@GenericGenerator(
	        name = "tp_tag_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "tp_tag_seq", value = "tp_tag_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "TipoTagId", unique = true)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nombre;

	public TipoTag() {}

	public TipoTag(int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}
	
	public TipoTag(String nombre) {
		this.nombre = nombre;
	}

	

}
