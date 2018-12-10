package com.itsight.domain;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "usuario.perfil",attributeNodes = {
	                  @NamedAttributeNode( value = "perfil" )}),
	@NamedEntityGraph(name = "usuario",attributeNodes = {}),
})
@Data
public class Usuario {

	@Id
	@GeneratedValue(generator="usuario_seq")
	@GenericGenerator(
	        name = "usuario_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "usuario_seq", value = "usuario_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "UsuarioId")
	private int id;
	@Column(nullable=false)
	private String nombres;
	@Column(nullable=false)
	private String apellidoPaterno;
	@Column(nullable=false)
	private String apellidoMaterno;
	@Column(nullable=false)
	private String username;
	@Column(nullable=false)
	private String password;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = false)
	private Date fechaCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = true)
	private Date fechaModificacion;
	@Column
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaUltimoAcceso;
	@Column(nullable = false)
	private boolean flagActivo;
	@Column(nullable = false)
	private boolean flagEliminado;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PerfilId")
	private Perfil perfil;
	
	@Transient
	@JsonSerialize
	private String nombreCompleto;
	
	@Transient
	private int fkPerfil;
	
	public Usuario() {}
	
	public Usuario(int id) {
		this.id = id;
	}
	
	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
	
	public void setFechaUltimoAcceso() {
		this.fechaUltimoAcceso = new Date();
	}

	public void setPerfil(int id) {
		this.perfil = new Perfil(id);
	}

	public String getNombreCompleto() {
		return this.apellidoPaterno + " " + this.apellidoMaterno + ", "+ this.nombres;
	}
}
