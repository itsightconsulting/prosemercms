package com.itsight.domain;

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
	@NamedEntityGraph(name = "resumen.contenidoWeb",attributeNodes = {
            @NamedAttributeNode( value = "contenidoWeb" )}),
	@NamedEntityGraph(name = "resumen",attributeNodes = {}),
})
public class Resumen {

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
	private ContenidoWeb contenidoWeb;
	
	public Resumen() {
		// TODO Auto-generated constructor stub
	}

	public Resumen(int id, String titulo, String resumen, String url, String rutaImagenPortada) {
		this.id = id;
		this.titulo = titulo;
		this.resumen = resumen;
		this.url = url;
		this.rutaImagenPortada = rutaImagenPortada;
	}
}
