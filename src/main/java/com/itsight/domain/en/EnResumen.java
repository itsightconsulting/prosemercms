package com.itsight.domain.en;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
@NamedEntityGraphs({
	@NamedEntityGraph(name = "enResumen.contenidoWeb",attributeNodes = {
            @NamedAttributeNode( value = "contenidoWeb" )}),
	@NamedEntityGraph(name = "enResumen",attributeNodes = {}),
})
public class EnResumen {

	@Id
	@Column(name = "ResumenId")
	private int id;
	@Column(nullable=false)
	private String titulo;
	@Column(nullable=false)
	private String resumen;
	@Column(nullable=false)
	private String url;
	@Column(nullable = true)
	private String nombreImagenPortada;
	@Column(nullable = true)
	private String rutaImagenPortada;
	
	// FK
	@JsonManagedReference
	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "ContenidoWebId")
	private EnContenidoWeb contenidoWeb;
	
	public EnResumen() {
		// TODO Auto-generated constructor stub
	}

	public EnResumen(int id, String titulo, String resumen, String url, String rutaImagenPortada) {
		this.id = id;
		this.titulo = titulo;
		this.resumen = resumen;
		this.url = url;
		this.rutaImagenPortada = rutaImagenPortada;
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
