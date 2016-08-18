package ort.proyecto.gestac.core.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Knowledge.class)
@Entity
public class Knowledge {

	@Id 
	@GeneratedValue
	private Long id;
	
	private String description;
	
	@JsonManagedReference
	@OneToMany(mappedBy="knowledge", fetch = FetchType.EAGER)
	private Set<KnowledgeEvaluation> evaluations;
	
	/**
	 * no bidireccional, por independencia de datos
	 */
	@ManyToOne
	private Source source;
	
	/**
	 * no bidireccional, por independencia de datos
	 */
	@ManyToOne
	private Issue issue;
	
	private double knowledgeScore;
	
	private int consideredEvaluations;
	
	private int totalEvaluations;

	
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

	public Set<KnowledgeEvaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(Set<KnowledgeEvaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public int getConsideredEvaluations() {
		return consideredEvaluations;
	}

	public void setConsideredEvaluations(int consideredEvaluations) {
		this.consideredEvaluations = consideredEvaluations;
	}

	public int getTotalEvaluations() {
		return totalEvaluations;
	}

	public void setTotalEvaluations(int totalEvaluations) {
		this.totalEvaluations = totalEvaluations;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public double getKnowledgeScore() {
		return knowledgeScore;
	}

	public void setKnowledgeScore(double knowledgeScore) {
		this.knowledgeScore = knowledgeScore;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
}
