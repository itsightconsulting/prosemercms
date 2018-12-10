package com.itsight.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Entity
@Data
@NamedEntityGraphs({
	@NamedEntityGraph(name = "contenidoWeb.usuario",attributeNodes = {
            @NamedAttributeNode( value = "usuario" )}),
	@NamedEntityGraph(name = "contenidoWeb.tipoContenido",attributeNodes = {
            @NamedAttributeNode( value = "tipoContenido" )}),
	@NamedEntityGraph(name = "contenidoWeb.all",attributeNodes = {
            @NamedAttributeNode( value = "tipoContenido" ),
            @NamedAttributeNode( value = "usuario" ),
            @NamedAttributeNode( value = "lstEstudio" )}),
	@NamedEntityGraph(name = "contenidoWeb",attributeNodes = {}),
})
public class ContenidoWeb {

	@Id
	@GeneratedValue(generator="contenido_web_seq")
	@GenericGenerator(
	        name = "contenido_web_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "contenido_web_seq", value = "contenido_web_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "ContenidoWebId")
	private int id;
	@Column(nullable=false)
	private String titulo;
	@Column(nullable=false)
	private String url;
	@Column(nullable=true)
	private String tags;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = false)
	private Date fechaCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = true)
	private Date fechaModificacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, updatable = true)
	private Date fechaContenido;
	@Column(nullable = false)
	private boolean flagActivo;
	@Column(nullable = false)
	private boolean flagEliminado;
	@Column(nullable = true)
	private boolean flagResumen;
	@Column(nullable = true)
	private boolean flagInicio;
	
	// FK
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TipoContenidoId", updatable = false)
	private TipoContenido tipoContenido;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UsuarioId", updatable = false)
	private Usuario usuario;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenidoWeb", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval=false)
	private List<Estudio> lstEstudio;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenidoWeb", cascade = {CascadeType.PERSIST}, orphanRemoval=false)
	private List<Capacitacion> lstCapacitacion;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenidoWeb", cascade = {CascadeType.PERSIST}, orphanRemoval=false)
	private List<Memoria> lstMemoria;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenidoWeb", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=false)
	private List<ContenidoImagen> lstContenidoImagen;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenidoWeb", cascade = {CascadeType.REMOVE}, orphanRemoval=true)
	private List<ContenidoArchivo> lstContenidoArchivo;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contenidoWeb", cascade = {CascadeType.PERSIST}, orphanRemoval=false)
	private List<Evento> lstEvento;
	
	@JsonBackReference
	@OneToOne(mappedBy = "contenidoWeb", fetch = FetchType.LAZY ,cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	private Resumen resumen;
	
	@Transient
	private int fkTipoContenido;
	
	@JsonSerialize
	@Transient
	private String resumenRaw;
	
	public ContenidoWeb() {
		// TODO Auto-generated constructor stub
	}

	public ContenidoWeb(int id, String url) {
		this.id = id;
		this.url = url;
	}

	public ContenidoWeb(int id, String titulo, String url) {
		this.id = id;
		this.titulo = titulo;
		this.url = url;
	}
	
	public ContenidoWeb(String titulo, String url, int id, String resumen) {
		this.id = id;
		this.titulo = titulo;
		this.url = url;
		this.resumenRaw = resumen;
	}
	
	public ContenidoWeb(int id, String titulo, String url, String tags) {
		this.id = id;
		this.titulo = titulo;
		this.url = url;
		this.tags = tags;
	}
	
	public ContenidoWeb(int id, String titulo, String url, String tags, String resumen) {
		this.id = id;
		this.titulo = titulo;
		this.url = url;
		this.tags = tags;
		this.resumenRaw = resumen;
	}
	
	public ContenidoWeb(int id, String titulo, boolean flagInicio) {
		this.id = id;
		this.titulo = titulo;
		this.flagInicio = flagInicio;
	}
	
	public ContenidoWeb(int id) {
		this.id = id;
	}

	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}

	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
	
	public void setUsuario(int id) {
		this.usuario = new Usuario(id);
	}
	
	public void setTipoContenido(TipoContenido tipoContenido) {
		this.tipoContenido = tipoContenido;
	}
	
	public void setTipoContenido(int id) {
		this.tipoContenido = new TipoContenido(id);
	}
	
	public void addEstudio(Estudio estudio) {
		estudio.setContenidoWeb(this);
        this.lstEstudio = new ArrayList<>();
        this.lstEstudio.add(estudio);
    }
    
    public void addCapacitacion(Capacitacion capacitacion) {
    	capacitacion.setContenidoWeb(this);
        this.lstCapacitacion = new ArrayList<>();
    	this.lstCapacitacion.add(capacitacion);
    }
    
    public void addMemoria(Memoria memoria) {
    	memoria.setContenidoWeb(this);
        this.lstMemoria = new ArrayList<>();
    	this.lstMemoria.add(memoria);
    }
    
    public void addEvento(Evento evento) {
    	evento.setContenidoWeb(this);
        this.lstEvento = new ArrayList<>();
    	this.lstEvento.add(evento);
    }
    
    public void addResumen(Resumen resumen) {
    	resumen.setContenidoWeb(this);
    	resumen.setTitulo(this.titulo);
    	resumen.setUrl(this.url);
    	this.resumen = resumen;
    }
    
    public void setUrl(int tipoContenidoId, String titulo) {
    	titulo = titulo.replaceAll("[¿?]", "");
    	switch (tipoContenidoId) {
		case 1:
			this.url = "web/estudio/"+titulo+"/";
			break;
		case 2:
			this.url = "web/capacitacion/"+titulo+"/";
			break;
		case 3:
			this.url = "web/evento/"+titulo+"/";
			break;
		}
    	 
    }
	
}
