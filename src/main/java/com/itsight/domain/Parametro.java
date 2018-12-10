package com.itsight.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class Parametro {
	
	@Id
	@GeneratedValue(generator="parametro_seq")
	@GenericGenerator(
	        name = "parametro_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "parametro_seq", value = "parametro_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "ParametroId")
	private int id;
	
	@Column(nullable = false, unique = true)
	private String clave;
	
	@Column(nullable = false)
	private String valor;
	
	@Column(nullable = true)
	private String modificadoPor;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Lima")
	@Temporal(TemporalType.TIMESTAMP)
//	@Column(nullable = true, updatable = true, columnDefinition="DATETIME")//MYSQL
	@Column(nullable = true, updatable = true)//PGSQL
	private Date fechaModificacion;
	
	public Parametro() {
		// TODO Auto-generated constructor stub
	}

	public Parametro(String clave, String valor) {
		this.clave = clave;
		this.valor = valor;
	}
	
	public void setFechaModificacion() {
		this.fechaModificacion = new Date();
	}
}
