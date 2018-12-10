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

import lombok.Data;

@NamedEntityGraphs({
	@NamedEntityGraph(name = "tag.tipo", attributeNodes = {
			@NamedAttributeNode(value = "tipoTag")
	}),
	@NamedEntityGraph(name = "tag", attributeNodes = {
			
	})
})
@Entity
@Data
public class Tag {
	
	@Id
	@GeneratedValue(generator="tag_seq")
	@GenericGenerator(
	        name = "tag_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "tag_seq", value = "tag_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "TagId", unique = true)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String nombre;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipoTagId")
	private TipoTag tipoTag;

	public Tag() {}

	public Tag(String nombre) {
		this.nombre = nombre;
	}
	
	public Tag(String nombre, int id) {
		this.nombre = nombre;
		this.tipoTag = new TipoTag(id);
	}
	
	public void setTipoTag(int id) {
		this.tipoTag = new TipoTag(id);
	}
	
	public void setTipoTag(TipoTag tipoTag) {
		this.tipoTag = tipoTag;
	}
	

}
