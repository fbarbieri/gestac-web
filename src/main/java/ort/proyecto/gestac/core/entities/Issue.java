package ort.proyecto.gestac.core.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

//import com.google.gson.annotations.Expose;

@Entity
public class Issue {

//	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
//	@Expose
	private String title;
	
//	@Expose
	private String description;
	
//	@Expose
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Subject> subjects = new HashSet<>();
	
//	@Expose
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Incident> incidents = new HashSet<>();
	
//	@Expose
	@ManyToOne
	private Gravity gravity;
	
	
}
