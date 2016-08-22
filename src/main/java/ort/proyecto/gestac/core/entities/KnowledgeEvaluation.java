package ort.proyecto.gestac.core.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = KnowledgeEvaluation.class)
@Entity
public class KnowledgeEvaluation {
	
	@Id 
	@GeneratedValue
	private Long id;
	
	@JsonBackReference
	@ManyToOne
	private Knowledge knowledge;
	
	private Timestamp date;
	
	private double simplicity;
	
	private double usedTime;
	
	private double reuse;

	public KnowledgeEvaluation(Knowledge knowledge, Timestamp date, double simplicity, double usedTime,
			double reuse) {
		super();
		this.knowledge = knowledge;
		this.date = date;
		this.simplicity = simplicity;
		this.usedTime = usedTime;
		this.reuse = reuse;
	}
	
	public KnowledgeEvaluation() {
		super();
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public double getSimplicity() {
		return simplicity;
	}

	public void setSimplicity(double simplicity) {
		this.simplicity = simplicity;
	}

	public double getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(double usedTime) {
		this.usedTime = usedTime;
	}

	public double getReuse() {
		return reuse;
	}

	public void setReuse(double reuse) {
		this.reuse = reuse;
	}	

}
