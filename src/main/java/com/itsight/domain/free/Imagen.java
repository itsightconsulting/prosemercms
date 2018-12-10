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
	@NamedEntityGraph(name = "imagen",
		attributeNodes = {
	}),
})
@Data
public class Imagen {

	@Id
	@GeneratedValue(generator="imagen_seq")
	@GenericGenerator(
	        name = "imagen_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "imagen_seq", value = "imagen_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "ImagenId")
	private int id;
	
	@Column(nullable = false)
	private String nombreMedia;
	
	@Column(nullable = true)
	private String realMediaPath;
	
	@Column(nullable = true)
	private String rutaMediaWeb;
	
	@Column(nullable = false)
	private String uuid;
	
	public Imagen() {}

	public Imagen(String nombreMedia, String rutaMediaWeb) {
		this.nombreMedia = nombreMedia;
		this.rutaMediaWeb = rutaMediaWeb;
	}
	
	public Imagen(int id, String nombreMedia, String rutaMediaWeb) {
		this.id = id;
		this.nombreMedia = nombreMedia;
		this.rutaMediaWeb = rutaMediaWeb;
	}
}
