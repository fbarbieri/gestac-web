package ort.proyecto.gestac.core.entities.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

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
	}
	
	@SuppressWarnings("unchecked")
	public List<Knowledge> getAllKnowledgesForIssue(String issueId) {
		List<Knowledge> result = null;
		result = em.createQuery("from Knowledge as k "
				+ "where k.issue.id=?1 order by k.knowledgeScore desc").
				setParameter(1,  Long.parseLong(issueId)).getResultList();
		if (result.size()==0) {
			result = null;
		}
		return result;
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
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void updateBestKnowledgesForIssues() {
		List<Object[]> list = em.createQuery(""
				+ "from Knowledge as knowledge, IssueBestKnowledge as best "
				+ "where knowledge.issue.id=best.issue.id and "
				+ "knowledge.knowledgeScore > best.knowledge.knowledgeScore")
				.getResultList();
		for (Object[] row : list) {
			IssueBestKnowledge best = (IssueBestKnowledge) row[1];
			Knowledge better = (Knowledge) row [0];
			best.setKnowledge(better);
			em.merge(best);
		}
		
//		List<Long> issuesWithoutBest = em.createQuery("select best.issue.id "
//				+ "from IssueBestKnowledge as best "
//				+ "where best.knowledge is null").getResultList();
//		for (Long id : issuesWithoutBest) {
//			
//		}
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
	
	@Override
	@Transactional
	public Knowledge update(Knowledge knowledge) {
		return em.merge(knowledge);
	}	
	
	@Override
	@Transactional
	public void addAsBestIfNull(Knowledge knowledge) {
		IssueBestKnowledge best = null;
		try{
			best = (IssueBestKnowledge) em.createQuery(""
					+ "from IssueBestKnowledge best "
					+ "where best.issue.id=?1 "
					+ "and best.knowledge is null").
					setParameter(1, knowledge.getIssue().getId()).
					getSingleResult();			
		} catch (Exception e) {
			//no result
		}
		if (best!=null) {
			best.setKnowledge(knowledge);
			em.merge(best);
		}
	}
	
	

}
