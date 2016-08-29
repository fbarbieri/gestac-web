package ort.proyecto.gestac.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Area.class)
@Entity
public class Area implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	private Long id;
		
	private String name;
	
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy="area", fetch = FetchType.EAGER)
	@OrderBy("name")
	private List<Subject> subjects = new ArrayList<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy="area", fetch = FetchType.EAGER)
	@OrderBy("name")
	private List<Incident> incidents = new ArrayList<>();
	//private Set<Incident> incidents = new HashSet<>();
	
	@JsonManagedReference
	@OneToMany(mappedBy="area", fetch = FetchType.EAGER)
	@OrderBy("name")
	private List<Source> sources = new ArrayList<>();

	
	public Area(Long id, String name, String description, List<Subject> subjects, List<Incident> incidents,
			List<Source> sources) {
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
	
	public Area(Long id) {
		super();
		this.id = id;
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

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Incident> getIncidents() {
		return incidents;
	}

	public void setIncidents(List<Incident> incidents) {
		this.incidents = incidents;
	}

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}
