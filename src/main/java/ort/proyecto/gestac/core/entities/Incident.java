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

import com.google.gson.annotations.Expose;

@Entity
public class Incident {

	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
	@Expose
	private String name;
	
	@Expose
	private String description;
	
	@Expose
	@OneToMany(mappedBy="incident", fetch = FetchType.EAGER)
	private Set<Gravity> gravities = new HashSet<>();
	
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
	
	
}
