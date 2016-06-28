package ort.proyecto.gestac.core.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

@Entity
public class Gravity {

	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
	@Expose
	private String description;
	
	@Expose
	private double value;
	
	@ManyToOne
	private Incident incident;
	
}
