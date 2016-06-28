package ort.proyecto.gestac.core.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

@Entity
public class Source {

	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
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
