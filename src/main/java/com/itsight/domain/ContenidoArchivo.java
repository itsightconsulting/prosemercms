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
	@NamedEntityGraph(name = "contenidoArchivo.contenidoWeb",
			attributeNodes = {
					@NamedAttributeNode(value = "contenidoWeb"),
			}),
	@NamedEntityGraph(name = "contenidoArchivo",
		attributeNodes = {
	}),
})
@Data
public class ContenidoArchivo {

	@Id
	@GeneratedValue(generator="contenido_archivo_seq")
	@GenericGenerator(
	        name = "contenido_archivo_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "contenido_archivo_seq", value = "contenido_archivo_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "ContenidoArchivoId")
	private int id;
	
	@Column(nullable = false)
	private String alias;
	
	@Column(nullable = false)
	private String nombreMedia;
	
	@Column(nullable = false)
	private String realMediaPath;
	
	@Column(nullable = false)
	private String rutaMediaWeb;
	
	@Column(nullable = false)
	private String uuid;
	
	@Column(nullable = false)
	private Integer orden;
	
	@Column(nullable = true)
	private String peso;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ContenidoWebId")
	private ContenidoWeb contenidoWeb;
	
	public ContenidoArchivo() {}
	
	public ContenidoArchivo(String alias, String rutaMediaWeb, String peso) {
		this.alias = alias;
		this.rutaMediaWeb = rutaMediaWeb;
		this.peso = peso;
	}
	
	public ContenidoArchivo(int id, String nombreMedia, String rutaMediaWeb, String peso) {
		this.id = id;
		this.nombreMedia = nombreMedia;
		this.rutaMediaWeb = rutaMediaWeb;
		this.peso = peso;
	}
	
	public ContenidoArchivo(int id) {
		this.id = id;
	}
	
	public ContenidoArchivo(String alias) {
		this.alias = alias;
	}
	
	public void setContenidoWeb(Integer id) {
		this.contenidoWeb = new ContenidoWeb(id);
	}
	
	public void setContenidoWeb(ContenidoWeb contenidoWeb) {
		this.contenidoWeb = contenidoWeb;
	}
}
