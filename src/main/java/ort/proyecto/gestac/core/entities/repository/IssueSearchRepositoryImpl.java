package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;

import ort.proyecto.gestac.core.entities.Issue;

public class IssueSearchRepositoryImpl implements IssueSearchRepository {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Issue> getIssuesBySubjectGravity(long subjectId, long gravityId) {
		List<Issue> list = em.createQuery("select issue from Issue as issue "
				+ "left join issue.subjects as subject "
				+ "where subject.id=?1 "
				+ "and issue.gravity.id=?2").
				setParameter(1, new Long(subjectId)).
				setParameter(2, new Long(gravityId)).
				getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Issue> getIssuesBySubjectIncident(long subjectId, long incidentId) {
		List<Issue> list = em.createQuery("select issue from Issue as issue "
				+ "left join issue.subjects as subject "
				+ "left join issue.incidents as incident "
				+ "where subject.id=?1 "
				+ "and incident.id=?2 ").
				setParameter(1, new Long(subjectId)).
				setParameter(2, new Long(incidentId)).
				getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Issue> getIssuesBySubjectIncidentGravity(long subjectId, long incidentId, long gravityId) {
//		Issue i = (Issue) em.find(Issue.class, Long.parseLong("1"));
//		
//		List<Issue> l = em.createQuery("select issue from Issue as issue where issue.id=?1").
//				setParameter(1, new Long(2)).getResultList();
//		
//		List<Issue> l2 = em.createQuery("select issue from Issue as issue where issue.gravity.id=?1").
//				setParameter(1, new Long(1)).getResultList();
//		
//		List<Issue> l3 = em.createQuery("select issue from Issue as issue "
//				+ "left join issue.subjects as subject "
//				+ "where subject.id=?1").
//				setParameter(1, new Long(3)).getResultList();
		
		List<Issue> list = em.createQuery("select issue from Issue as issue "
				+ "left join issue.subjects as subject "
				+ "left join issue.incidents as incident "
				+ "where subject.id=?1 "
				+ "and incident.id=?2 "
				+ "and issue.gravity.id=?3").
				setParameter(1, new Long(subjectId)).
				setParameter(2, new Long(incidentId)).
				setParameter(3, new Long(gravityId)).
				getResultList();
		
		return list;
	}
	
	public List<Issue> getIssuesWithoutKnowledgeForSource(long sourceId, long areaId) {
		
//		List<Object> withKnowledge = 
//				em.createQuery("select k.issue from Knowledge as k where k.issue is not null and k.source.id=?1").setParameter(1, new Long(sourceId)).getResultList();

		List<Issue> list = em.createQuery("select issue from Issue as issue join issue.subjects subjects "
				+ "where subjects.area.id=?1 "
				+ " and issue.id not in (select k.issue.id from Knowledge as k where k.issue is not null)")
				.setParameter(1, new Long(areaId)).getResultList();
		
//		List<Issue> list = em.createQuery("select issue from Issue as issue "
//				+ "where issue.subjects").getResultList();
		return list;
	}
}
