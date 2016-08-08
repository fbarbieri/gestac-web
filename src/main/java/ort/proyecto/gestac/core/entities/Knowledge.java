package ort.proyecto.gestac.core.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//import com.google.gson.annotations.Expose;

@Entity
public class Knowledge {

//	@Expose
	@Id 
	@GeneratedValue
	private Long id;
	
	private String description;
	
	@OneToMany(mappedBy="knowledge", fetch = FetchType.EAGER)
	private Set<KnowledgeEvaluation> evaluations;

	
	public Knowledge(Long id) {
		super();
		this.id = id;
	}
	
	public Knowledge() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
