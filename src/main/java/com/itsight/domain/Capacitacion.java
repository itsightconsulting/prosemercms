package com.itsight.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "capacitacion.beneficiario",attributeNodes = {
			@NamedAttributeNode( value = "beneficiario" )}),
	@NamedEntityGraph(name = "capacitacion.estudio",attributeNodes = {
						@NamedAttributeNode( value = "estudio" )}),
	@NamedEntityGraph(name = "capacitacion.contenidoWeb",attributeNodes = {
            			@NamedAttributeNode( value = "contenidoWeb" )}),
	@NamedEntityGraph(name = "capacitacion",attributeNodes = {}),
})
@Data
public class Capacitacion {

	@Id
	@GeneratedValue(generator="capacitacion_seq")
	@GenericGenerator(
	        name = "capacitacion_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "capacitacion_seq", value = "capacitacion_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "CapacitacionId")
	private int id;
	@Column(nullable=false)
	private String tituloPrincipal;
	@Column(nullable=false)
	private String tituloLargo;
	@Column(nullable=true)
	private String tituloEstudioReferencia;
	@Lob
	@Column(nullable=false)
	private String descripcion;
	@Column(nullable=false)
	private String tags;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Lima")
	@Temporal(TemporalType.DATE)
	@Column(nullable = true, updatable = true)
	private Date fechaCapacitacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = false)
	private Date fechaCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = true)
	private Date fechaModificacion;
	@Column(nullable = false)
	private boolean flagActivo;
	@Column(nullable = false)
	private boolean flagEliminado;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "ContenidoWebId")
	private ContenidoWeb contenidoWeb;
	
	//FK
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EstudioId")
	private Estudio estudio;
		
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BeneficiarioId")
	private Beneficiario beneficiario;
	
	@Transient
	private String fkEstudio;
	
	@Transient
	private String fkBeneficiario;
	
	public Capacitacion() {
		// TODO Auto-generated constructor stub
	}
	
	public Capacitacion(String tituloPrincipal, String tituloLargo, String tituloEstudioReferencia, String descripcion) {
		this.tituloPrincipal = tituloPrincipal;
		this.tituloLargo = tituloLargo;
		this.tituloEstudioReferencia = tituloEstudioReferencia;
		this.descripcion = descripcion;
	}
	
	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
	
	public void setContenidoWeb(ContenidoWeb contenidoWeb) {
		this.contenidoWeb = contenidoWeb;
	}
	
	public void setContenidoWeb(int id) {
		this.contenidoWeb = new ContenidoWeb(id);
	}

	public void setEstudio(int id) {
		// TODO Auto-generated method stub
		this.estudio = new Estudio(id);
	}
	
}
