package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import ort.proyecto.gestac.core.entities.Knowledge;

public interface KnowledgeDataSource {
	
	public Knowledge getBestKnowledgeForIssue(String issueId);

	public void addEvaluationToKnowledge(String knowledgeId, String simplicity, String usedTime, String reuse);
	
	public List<Knowledge> searchKnowledgesToUpdate();
	
	public Knowledge update(Knowledge knowledge);

	public void updateBestKnowledgesForIssues();

}
