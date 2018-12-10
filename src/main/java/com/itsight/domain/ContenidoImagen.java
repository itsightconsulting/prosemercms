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
	@NamedEntityGraph(name = "contenidoImagen.contenidoWeb",
			attributeNodes = {
					@NamedAttributeNode(value = "contenidoWeb"),
			}),
	@NamedEntityGraph(name = "contenidoImagen",
		attributeNodes = {
	}),
})
@Data
public class ContenidoImagen {

	@Id
	@GeneratedValue(generator="contenido_imagen_seq")
	@GenericGenerator(
	        name = "contenido_imagen_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "contenido_imagen_seq", value = "contenido_imagen_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "ContenidoImagenId")
	private int id;
	
	@Column(nullable = false)
	private String nombreMedia;
	
	@Column(nullable = false)
	private String realMediaPath;
	
	@Column(nullable = false)
	private String rutaMediaWeb;
	
	@Column(nullable = false)
	private String uuid;
	
	@Column(nullable = true)
	private Integer  orden;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ContenidoWebId")
	private ContenidoWeb contenidoWeb;
	
	public ContenidoImagen() {}

	public ContenidoImagen(String nombreMedia, String rutaMediaWeb) {
		this.nombreMedia = nombreMedia;
		this.rutaMediaWeb = rutaMediaWeb;
	}
	
	public ContenidoImagen(int id, String nombreMedia, String rutaMediaWeb) {
		this.id = id;
		this.nombreMedia = nombreMedia;
		this.rutaMediaWeb = rutaMediaWeb;
	}
	
	public ContenidoImagen(int id) {
		this.id = id;
	}
	
	public void setContenidoWeb(Integer id) {
		this.contenidoWeb = new ContenidoWeb(id);
	}
	
	public void setContenidoWeb(ContenidoWeb contenidoWeb) {
		this.contenidoWeb = contenidoWeb;
	}
}
