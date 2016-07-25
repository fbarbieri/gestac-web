package ort.proyecto.gestac.core.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
