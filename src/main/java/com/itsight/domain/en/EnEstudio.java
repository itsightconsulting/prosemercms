package com.itsight.domain.en;

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
import com.itsight.domain.Beneficiario;
import com.itsight.domain.TipoEstudio;

import lombok.Data;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "enEstudio.beneficiario",attributeNodes = {
	                  @NamedAttributeNode( value = "beneficiario" )}),
	@NamedEntityGraph(name = "enEstudio.tipo",attributeNodes = {
            @NamedAttributeNode( value = "tipoEstudio" )}),
	@NamedEntityGraph(name = "enEstudio.contenidoWeb",attributeNodes = {
            @NamedAttributeNode( value = "contenidoWeb" )}),
	@NamedEntityGraph(name = "enEstudio.all",attributeNodes = {
            @NamedAttributeNode( value = "contenidoWeb" ),
            @NamedAttributeNode( value = "tipoEstudio" )}),
	@NamedEntityGraph(name = "enEstudio",attributeNodes = {}),
})
@Data
public class EnEstudio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator="en_estudio_seq")
	@GenericGenerator(
	        name = "en_estudio_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "en_estudio_seq", value = "en_estudio_seq"),
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
	private EnContenidoWeb contenidoWeb;
	
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
	
	@Column
	private String tags;
	
	public EnEstudio() {}
	
	public EnEstudio(int id) {
		this.id = id;
	}
	
	public EnEstudio(int id, String tituloPrincipal) {
		this.id = id;
		this.tituloPrincipal = tituloPrincipal;
	}
	
	public EnEstudio(int id, String tituloPrincipal, int beneficiario) {
		this.id = id;
		this.tituloPrincipal = tituloPrincipal;
		this.beneficiario = new Beneficiario(beneficiario);
	}
	
	public EnEstudio(int id, int tipoEstudio, int beneficiario) {
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
	
	public void setEnContenidoWeb(EnContenidoWeb contenidoWeb) {
		this.contenidoWeb = contenidoWeb;
	}
	
	public void setEnContenidoWeb(int id) {
		this.contenidoWeb = new EnContenidoWeb(id);
	}
	
	public void setTipoEstudio(TipoEstudio tipoEstudio) {
		this.tipoEstudio = tipoEstudio;
	}
	
	public void setTipoEstudio(int id) {
		this.tipoEstudio = new TipoEstudio(id);
	}

	public void setContenidoWeb(int contenidoWebId) {
		// TODO Auto-generated method stub
		this.contenidoWeb = new EnContenidoWeb(contenidoWebId);
	}

	public void setContenidoWeb(EnContenidoWeb enContenidoWeb) {
		// TODO Auto-generated method stub
		this.contenidoWeb = enContenidoWeb;
	}
	
}
