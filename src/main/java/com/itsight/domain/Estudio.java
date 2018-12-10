package com.itsight.domain;

import java.io.Serializable;
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
	@NamedEntityGraph(name = "estudio.beneficiario",attributeNodes = {
	                  @NamedAttributeNode( value = "beneficiario" )}),
	@NamedEntityGraph(name = "estudio.tipo",attributeNodes = {
            @NamedAttributeNode( value = "tipoEstudio" )}),
	@NamedEntityGraph(name = "estudio.contenidoWeb",attributeNodes = {
            @NamedAttributeNode( value = "contenidoWeb" )}),
	@NamedEntityGraph(name = "estudio.all",attributeNodes = {
            @NamedAttributeNode( value = "contenidoWeb" ),
            @NamedAttributeNode( value = "tipoEstudio" )}),
	@NamedEntityGraph(name = "estudio",attributeNodes = {}),
})
@Data
public class Estudio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator="estudio_seq")
	@GenericGenerator(
	        name = "estudio_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "estudio_seq", value = "estudio_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "EstudioId")
	private int id;
	@Column(nullable=false)
	private String tituloPrincipal;
	@Column(nullable=false)
	private String tituloLargo;
	@Column(nullable=false)
	private String consultor;
	@Column(nullable=true)
	private String responsableEntidad;
	@Lob
	@Column(nullable=false)
	private String alcance;
	@Column(nullable = true)
	private String nombreImagenPortada;
	@Column(nullable = true)
	private String rutaImagenPortada;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "America/Lima")
	@Temporal(TemporalType.DATE)
	@Column(nullable = true, updatable = true)
	private Date fechaEstudio;
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
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BeneficiarioId")
	private Beneficiario beneficiario;
	
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TipoEstudioId")
	private TipoEstudio tipoEstudio;
	
	@Transient
	private int fkBeneficiario;
	
	@Transient
	private int fkTipoEstudio;

	@Column()
	private String tags;
	
	public Estudio() {}
	
	public Estudio(int id) { this.id = id; }
	
	public Estudio(int id, String tituloPrincipal) {
		this.id = id;
		this.tituloPrincipal = tituloPrincipal;
	}
	
	public Estudio(int id, String tituloPrincipal, int beneficiario) {
		this.id = id;
		this.tituloPrincipal = tituloPrincipal;
		this.beneficiario = new Beneficiario(beneficiario);
	}
	
	public Estudio(int id, int tipoEstudio, int beneficiario) {
		this.id = id;
		this.tipoEstudio = new TipoEstudio(tipoEstudio);
		this.beneficiario = new Beneficiario(beneficiario);
	}
	
	public void setFechaCreacion() {
		this.fechaCreacion = new Date();
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}

	public void setBeneficiario(int id) {
		this.beneficiario = new Beneficiario(id);
	}
	
	public void setContenidoWeb(ContenidoWeb contenidoWeb) {
		this.contenidoWeb = contenidoWeb;
	}
	
	public void setContenidoWeb(int id) {
		this.contenidoWeb = new ContenidoWeb(id);
	}
	
	public void setTipoEstudio(TipoEstudio tipoEstudio) {
		this.tipoEstudio = tipoEstudio;
	}
	
	public void setTipoEstudio(int id) {
		this.tipoEstudio = new TipoEstudio(id);
	}
	
}
