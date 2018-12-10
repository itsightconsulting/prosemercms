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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@NamedEntityGraphs({
	@NamedEntityGraph(name = "quienesSomos", attributeNodes = {})
})
@Entity
@Data
public class QuienesSomos {

	@Id
	@GeneratedValue(generator = "quienes_somos_seq")
	@GenericGenerator(
			name = "quienes_somos_seq",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "prefer_sequence_per_entity", value = "true"),
					@Parameter(name = "quienes_somos_seq", value = "quienes_somos_seq"),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@Column(name = "QuienesSomosId")
	private int id;
	@Column(nullable = false, updatable = false, unique = true)
	private String nombreMenu;
	@Lob
	@Column(nullable = true)
	private String contenido;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = false)
	private Date fechaCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = true)
	private Date fechaModificacion;
	@Column(nullable = true, updatable = true)
	private String modificadoPor;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ContenidoWebId")
	private ContenidoWeb contenidoWeb;
	public QuienesSomos () {}
	
	public QuienesSomos(String nombreMenu, Date fechaCreacion) {
		this.nombreMenu = nombreMenu;
		this.fechaCreacion = fechaCreacion;
	}
	
	public QuienesSomos(String nombreMenu, Date fechaCreacion, ContenidoWeb contenidoWeb) {
		this.nombreMenu = nombreMenu;
		this.fechaCreacion = fechaCreacion;
		this.contenidoWeb = contenidoWeb;
	}
	
	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
}
