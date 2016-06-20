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

	public Area(Long id, String name, String description, Set<Subject> subjects) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.subjects = subjects;
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

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", description=" + description + ", subjects=" + subjects + "]";
	}
	
	
	
	
}
