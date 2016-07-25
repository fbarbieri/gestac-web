package ort.proyecto.gestac.core.entities.repository;

import ort.proyecto.gestac.core.entities.Knowledge;

public interface KnowledgeDataSource {
	
	public Knowledge getBestKnowledgeForIssue(String issueId);

}
