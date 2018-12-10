package com.itsight.domain.en;

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
	@NamedEntityGraph(name = "enMemoria",attributeNodes = {}),
})
@Table(name = "EnMemoriaProyecto")
@Data
public class EnMemoria {

	@Id
	@GeneratedValue(generator="en_memoria_proyecto_seq")
	@GenericGenerator(
	        name = "en_memoria_proyecto_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "en_memoria_proyecto_seq", value = "en_memoria_proyecto_seq"),
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ContenidoWebId")
	private EnContenidoWeb contenidoWeb;
	
	public EnMemoria() {
		// TODO Auto-generated constructor stub
	}
	
	public EnMemoria(String tituloPrincipal, EnContenidoWeb contenidoWeb) {
		// TODO Auto-generated constructor stub
		this.tituloPrincipal = tituloPrincipal;
		this.contenidoWeb = contenidoWeb;
	}
	
	public EnMemoria(String tituloPrincipal, String contenido, String nombreArchivo, String rutaArchivo,
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
