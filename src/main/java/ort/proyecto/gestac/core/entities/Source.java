package ort.proyecto.gestac.core.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import com.google.gson.annotations.Expose;

@Entity
public class Source {

//	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	private Area area;
	
	private String name;
	
	private String mail;
	/**
	 * TODO completar
	 */
	
	private Timestamp updated;
	
	private Timestamp evaluationUpdated;
	
	private boolean onUpdate;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public Timestamp getEvaluationUpdated() {
		return evaluationUpdated;
	}

	public void setEvaluationUpdated(Timestamp evaluationUpdated) {
		this.evaluationUpdated = evaluationUpdated;
	}

	public boolean isOnUpdate() {
		return onUpdate;
	}

	public void setOnUpdate(boolean onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	
	
}
