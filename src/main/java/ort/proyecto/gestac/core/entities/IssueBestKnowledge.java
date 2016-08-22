package ort.proyecto.gestac.core.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class IssueBestKnowledge {
	
	@Id 
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Issue issue;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Knowledge knowledge;
	
	private Timestamp date;
	
	public IssueBestKnowledge(Long id, Issue issue, Knowledge knowledge, Timestamp date) {
		super();
		this.id = id;
		this.issue = issue;
		this.knowledge = knowledge;
		this.date = date;
	}
	
	public IssueBestKnowledge() {
		super();
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	

}
