package ort.proyecto.gestac.core.entities;

public class NewKnowledgeRequestHolder {

	private Knowledge knowledge;
	
	private KnowledgeEvaluation evaluation;
	
	private Source source;
	
	private Issue issue;

	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public KnowledgeEvaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(KnowledgeEvaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
}
