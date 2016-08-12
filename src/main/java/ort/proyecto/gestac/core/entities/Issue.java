package ort.proyecto.gestac.core.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


@Entity
public class Issue {

	@Id 
	@GeneratedValue
	private Long id;
	
	private String title;
	
	private String description;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Subject> subjects = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Incident> incidents = new HashSet<>();
	
	@ManyToOne
	private Gravity gravity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}

	public Set<Incident> getIncidents() {
		return incidents;
	}

	public void setIncidents(Set<Incident> incidents) {
		this.incidents = incidents;
	}

	public Gravity getGravity() {
		return gravity;
	}

	public void setGravity(Gravity gravity) {
		this.gravity = gravity;
	}
	
	
}
