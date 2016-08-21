package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;

import ort.proyecto.gestac.core.entities.Issue;

public class IssueSearchDataSourceImpl implements IssueSearchDataSource {

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
	
	@SuppressWarnings("unchecked")
	public List<Issue> getIssuesWithoutKnowledgeForSource(long sourceId, long areaId) {
		List<Issue> list = em.createQuery("select issue from Issue as issue join issue.subjects subjects "
				+ "where subjects.area.id=?1 "
				+ " and issue.id not in (select k.issue.id from Knowledge as k where k.issue is not null)")
				.setParameter(1, new Long(areaId)).getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Issue> getIssuesWithKnowledgeByOther(long sourceId, long areaId) {
		List<Issue> list = em.createQuery("select issue from Issue as issue join issue.subjects subjects "
				+ "where subjects.area.id=?1 "
				+ " and issue.id in (select k.issue.id from Knowledge as k where k.issue is not null)"
				+ " and issue.id not in (select k.issue.id from Knowledge as k where k.issue is not null and k.source.id=?2)")
				.setParameter(1, new Long(areaId))
				.setParameter(2, new Long(sourceId))
				.getResultList();
		return list;
	}
}
