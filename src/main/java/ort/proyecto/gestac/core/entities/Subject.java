package ort.proyecto.gestac.core.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

@Entity
public class Subject implements Serializable {
	
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
	
	@ManyToOne
	private Area area;

	
	public Subject(Long id, String name, Area area) {
		super();
		this.id = id;
		this.name = name;
		this.area = area;
	}
	
	public Subject() {
	
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
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Subject [id=" + id + ", name=" + name + ", area=" + area + "]";
	}
	
	
	
}
