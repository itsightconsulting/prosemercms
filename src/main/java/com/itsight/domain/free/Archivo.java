package com.itsight.domain.free;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "archivo",
		attributeNodes = {
	}),
})
@Data
public class Archivo {

	@Id
	@GeneratedValue(generator="archivo_seq")
	@GenericGenerator(
	        name = "archivo_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "archivo_seq", value = "archivo_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "ArchivoId")
	private int id;
	
	@Column(nullable = false)
	private String alias;
	
	@Column(nullable = false)
	private String nombreMedia;
	
	@Column(nullable = true)
	private String realMediaPath;
	
	@Column(nullable = true)
	private String rutaMediaWeb;
	
	@Column(nullable = false)
	private String uuid;
	
	@Column(nullable = true)
	private String peso;
	
	public Archivo() {}
	
	public Archivo(String alias, String rutaMediaWeb, String peso) {
		this.alias = alias;
		this.rutaMediaWeb = rutaMediaWeb;
		this.peso = peso;
	}
	
	public Archivo(int id, String nombreMedia, String rutaMediaWeb, String peso) {
		this.id = id;
		this.nombreMedia = nombreMedia;
		this.rutaMediaWeb = rutaMediaWeb;
		this.peso = peso;
	}
	
	public Archivo(int id) {
		this.id = id;
	}
	
	public Archivo(String alias) {
		this.alias = alias;
	}
}
