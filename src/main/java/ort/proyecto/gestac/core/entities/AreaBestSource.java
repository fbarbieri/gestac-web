package ort.proyecto.gestac.core.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class AreaBestSource {

	@Id 
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Area area;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Source source;
	
	private Timestamp date;
	
	public AreaBestSource(Long id, Area area, Source source, Timestamp date) {
		super();
		this.id = id;
		this.area = area;
		this.source = source;
		this.date = date;
	}
	
	public AreaBestSource() {
		super();
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

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}
	
}
