package ort.proyecto.gestac.core.entities.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.IssueBestKnowledge;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;

public class KnowledgeDataSourceImpl implements KnowledgeDataSource {

	@PersistenceContext
	private EntityManager em;
	
	public Knowledge getBestKnowledgeForIssue(String issueId) {
		
		IssueBestKnowledge best = (IssueBestKnowledge) em.createQuery("select best from IssueBestKnowledge as best "
				+ "where issue.id=?1").
				setParameter(1, Long.parseLong(issueId)).
				getSingleResult();
		
		return best.getKnowledge();
		/**
		 * TODO seguir acá!
		 */
	}
	
	@SuppressWarnings("unchecked")
	public List<Knowledge> searchKnowledgesToUpdate() {
		//SEARCH_KNOWLEDGES_TO_UPDATE
		List<Knowledge> result = null;
		result = em.createQuery("select toUpdate from Knowledge as toUpdate "
				+ "where consideredEvaluations<totalEvaluations").getResultList();
		
		if (result.size()==0) {
			result = null;
		}
		
		return result;
	}

	@Override
	@Transactional
	public void addEvaluationToKnowledge(String knowledgeId, String simplicity, String usedTime, String reuse) {
		Knowledge knowledge = em.find(Knowledge.class, Long.parseLong(knowledgeId));
		KnowledgeEvaluation evaluation = new KnowledgeEvaluation(knowledge, new Timestamp(System.currentTimeMillis()), 
				Double.parseDouble(simplicity), Double.parseDouble(usedTime), Double.parseDouble(reuse));
		knowledge.getEvaluations().add(evaluation);
		em.persist(evaluation);
		
		knowledge.setTotalEvaluations(knowledge.getTotalEvaluations()+1);
		em.merge(knowledge);
	}
	
	
	
}
