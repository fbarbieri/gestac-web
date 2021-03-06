package ort.proyecto.gestac.core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Incident {

	@Id 
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy="incident", fetch = FetchType.EAGER)
	@OrderBy("description")
	private List<Gravity> gravities = new ArrayList<>();
	
	@JsonBackReference
	@ManyToOne
	private Area area;
	
	public Incident() {
		super();
	}
	
	public Incident(Long id) {
		super();
		this.id = id;
	}

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

	public List<Gravity> getGravities() {
		return gravities;
	}

	public void setGravities(List<Gravity> gravities) {
		this.gravities = gravities;
	}
	
	
}
