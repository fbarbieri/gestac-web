package ort.proyecto.gestac.core.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.gson.annotations.Expose;

@Entity
public class Area implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	@Id 
	@GeneratedValue
	private Long id;
		
	@Expose
	private String name;
	
	@Expose
	private String description;
	
	@Expose
	@OneToMany(mappedBy="area", fetch = FetchType.EAGER)
	private Set<Subject> subjects = new HashSet<>();
	
	@Expose
	@OneToMany(mappedBy="area", fetch = FetchType.EAGER)
	private Set<Incident> incidents = new HashSet<>();
	
	@Expose
	@OneToMany(mappedBy="area", fetch = FetchType.EAGER)
	private Set<Source> sources = new HashSet<>();

	
	public Area(Long id, String name, String description, Set<Subject> subjects, Set<Incident> incidents,
			Set<Source> sources) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.subjects = subjects;
		this.incidents = incidents;
		this.sources = sources;
	}
	
	public Area() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<Source> getSources() {
		return sources;
	}

	public void setSources(Set<Source> sources) {
		this.sources = sources;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

	
	
}
