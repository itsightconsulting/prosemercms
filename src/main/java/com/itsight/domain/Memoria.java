package com.itsight.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "memoria",attributeNodes = {}),
})
@Table(name = "MemoriaProyecto")
@Data
public class Memoria {

	@Id
	@GeneratedValue(generator="memoria_proyecto_seq")
	@GenericGenerator(
	        name = "memoria_proyecto_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "memoria_proyecto_seq", value = "memoria_proyecto_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "MemoriaProyectoId")
	private int id;
	@Column(nullable=false)
	private String tituloPrincipal;
	@Lob
	@Column(nullable=true)
	private String contenido;
	@Column(nullable = true)
	private String nombreImagenPortada;
	@Column(nullable = true)
	private String rutaImagenPortada;
	@Column(nullable = true)
	private String nombreArchivo;
	@Column(nullable = true)
	private String rutaArchivo;
	@Column(nullable = true)
	private String peso;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = false)
	private Date fechaCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = true)
	private Date fechaModificacion;
	@Column(nullable = true)
	private String modificadoPor;
	//FK
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ContenidoWebId")
	private ContenidoWeb contenidoWeb;
	
	public Memoria() {
		// TODO Auto-generated constructor stub
	}
	
	public Memoria(String tituloPrincipal, ContenidoWeb contenidoWeb) {
		// TODO Auto-generated constructor stub
		this.tituloPrincipal = tituloPrincipal;
		this.contenidoWeb = contenidoWeb;
	}
	
	public Memoria(String tituloPrincipal, String contenido, String nombreArchivo, String rutaArchivo,
			String peso) {
		this.tituloPrincipal = tituloPrincipal;
		this.contenido = contenido;
		this.nombreArchivo = nombreArchivo;
		this.rutaArchivo = rutaArchivo;
		this.peso = peso;
	}

	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
	
}
