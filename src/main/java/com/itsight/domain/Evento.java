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
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@NamedEntityGraphs({
	@NamedEntityGraph(name = "evento", attributeNodes = {})
})
@Entity
@Data
public class Evento {

	@Id
	@GeneratedValue(generator = "evento_seq")
	@GenericGenerator(
			name = "evento_seq",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@Parameter(name = "prefer_sequence_per_entity", value = "true"),
					@Parameter(name = "evento_seq", value = "evento_seq"),
					@Parameter(name = "initial_value", value = "1"),
					@Parameter(name = "increment_size", value = "1")
			}
	)
	@Column(name = "EventoId")
	private int id;
	@Column(nullable = false)
	private String tituloPrincipal;
	@Column(nullable = false)
	private String tituloLargo;
	@Lob
	@Column(nullable = false)
	private String descripcion;
	@Column(nullable = false)
	private String tags;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Lima" )
	@Temporal(TemporalType.DATE)
	@Column(nullable = true, updatable = true)
	private Date fechaEvento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = false)
	private Date fechaCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss", timezone = "America/Lima")
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
	
	public Evento () {}

	public Evento(String tituloLargo, String descripcion, Date fechaEvento) {
		this.tituloLargo = tituloLargo;
		this.descripcion = descripcion;
		this.fechaEvento = fechaEvento;
	}
	
	public Evento(int id, String tituloPrincipal, String tituloLargo, String descripcion, String tags, Date fechaEvento,
			int contenidoWebId) {
		this.id = id;
		this.tituloPrincipal = tituloPrincipal;
		this.tituloLargo = tituloLargo;
		this.descripcion = descripcion;
		this.tags = tags;
		this.fechaEvento = fechaEvento;
		this.contenidoWeb = new ContenidoWeb(contenidoWebId);
	}
	
	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaCreacion = new Date();
	}
}
