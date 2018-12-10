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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "logro.beneficiario",attributeNodes = {
	                  @NamedAttributeNode( value = "beneficiario" )}),
	@NamedEntityGraph(name = "logro.estudio",attributeNodes = {
            @NamedAttributeNode( value = "estudio" )}),
	@NamedEntityGraph(name = "logro.all",attributeNodes = {
            @NamedAttributeNode( value = "beneficiario" ),
            @NamedAttributeNode( value = "estudio" )}),
	@NamedEntityGraph(name = "logro",attributeNodes = {}),
})
@Data
public class Logro {

	@Id
	@GeneratedValue(generator="logro_seq")
	@GenericGenerator(
	        name = "logro_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "logro_seq", value = "logro_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "LogroId")
	private int id;
	@Lob
	@Column(nullable = false)
	private String descripcion;
	@Column(nullable = false)
	private String resumen;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Lima", locale = "es")
	@Temporal(TemporalType.DATE)
	@Column(nullable = true, updatable = true)
	private Date fechaLogro;
	@Column(nullable = true)
	private String multiple;
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
	@Column(nullable = false)
	private boolean flagCompartido;
	//FK
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EstudioId")
	private Estudio estudio;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BeneficiarioId")
	private Beneficiario beneficiario;

	public Logro() {}
	
	public Logro(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
	
	public void setBeneficiario(Beneficiario beneficiario) {
		this.beneficiario = beneficiario;
	}
	
	public void setBeneficiario(int beneficiarioId) {
		this.beneficiario = new Beneficiario(beneficiarioId);
	}
	
	public void setEstudio(Estudio estudio) {
		this.estudio = estudio;
	}
	
	public void setEstudio(int estudioId) {
		this.estudio = new Estudio(estudioId);
	}
	
}
