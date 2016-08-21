package ort.proyecto.gestac.core.entities.repository;

import java.util.List;

import ort.proyecto.gestac.core.entities.Issue;

public interface IssueSearchDataSource {

	public List<Issue> getIssuesBySubjectIncidentGravity(long subjectId, long incidentId, long gravityId);
	
	public List<Issue> getIssuesBySubjectIncident(long subjectId, long incidentId);
	
	public List<Issue> getIssuesBySubjectGravity(long subjectId, long gravityId);
	
	public List<Issue> getIssuesWithoutKnowledgeForSource(long sourceId, long areaId);
	
	public List<Issue> getIssuesWithKnowledgeByOther(long sourceId, long areaId);
	
}
