package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.IssueBestKnowledge;
import ort.proyecto.gestac.core.entities.Knowledge;

public class KnowledgeDataSourceImpl implements KnowledgeDataSource {

	@PersistenceContext
	private EntityManager em;
	
	public Knowledge getBestKnowledgeForIssue(String issueId) {
		
		IssueBestKnowledge best = (IssueBestKnowledge) em.createQuery("select best from IssueBestKnowledge as best "
				+ "where issue.id?=1").
				setParameter(1, Long.parseLong(issueId)).
				getSingleResult();
		
		return best.getKnowledge();
		/**
		 * TODO seguir acá!
		 */
	}
	
}
