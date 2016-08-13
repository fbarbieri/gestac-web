package ort.proyecto.gestac.core.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Incident {

	@Id 
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy="incident", fetch = FetchType.EAGER)
	private Set<Gravity> gravities = new HashSet<>();
	
	@JsonBackReference
	@ManyToOne
	private Area area;
	

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Gravity> getGravities() {
		return gravities;
	}

	public void setGravities(Set<Gravity> gravities) {
		this.gravities = gravities;
	}
	
	
}
