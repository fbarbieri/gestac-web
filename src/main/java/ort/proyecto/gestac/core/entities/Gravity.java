package ort.proyecto.gestac.core.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.google.gson.annotations.Expose;

@Entity
public class Gravity {

//	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
//	@Expose
	private String description;
	
//	@Expose
	private double value;
	
	@JsonIgnore
	@ManyToOne
	private Incident incident;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Incident getIncident() {
		return incident;
	}

	public void setIncident(Incident incident) {
		this.incident = incident;
	}
	
}
