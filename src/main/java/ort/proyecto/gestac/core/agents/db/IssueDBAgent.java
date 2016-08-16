package ort.proyecto.gestac.core.agents.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.repository.IssueSearchRepository;

public class IssueDBAgent extends GestacAgent {
	
	@Autowired
	private IssueSearchRepository issueSearch;	
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new IssueDBAgentBehaviour());
	}
	
	class IssueDBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = blockingReceive();
            String content = message.getContent();
            String[] parameters = content.split("&");
            String operation = parameters[0];
            switch (operation){
            case DBAgentOperations.GET_ISSUES_SUBJECT_INCIDENT_GRAVITY:
        		List<Issue> issuesSubjectIncidentGravity = issueSearch.getIssuesBySubjectIncidentGravity
        				(Long.parseLong(parameters[1]), Long.parseLong(parameters[2]), Long.parseLong(parameters[3]));
        		sendReply(issuesSubjectIncidentGravity, message);
        		break;
        	case DBAgentOperations.GET_ISSUES_SUBJECT_INCIDENT:
        		List<Issue> issuesSubjectIncident = issueSearch.getIssuesBySubjectIncident(Long.parseLong(parameters[1]), 
        				Long.parseLong(parameters[2]));
        		sendReply(issuesSubjectIncident, message);
        		break;
        	case DBAgentOperations.GET_ISSUES_SUBJECT_GRAVITY:
        		List<Issue> issuesSubjectGravity = issueSearch.getIssuesBySubjectGravity(Long.parseLong(parameters[1]), 
        				Long.parseLong(parameters[2]));
        		sendReply(issuesSubjectGravity, message);
        		break;
        	case DBAgentOperations.GET_ISSUES_FOR_SOURCE_WITHOUT_KNOWLEDGE:
        		List<Issue> issuesForSource = issueSearch.getIssuesWithoutKnowledgeForSource(Long.parseLong(parameters[1]), 
        				Long.parseLong(parameters[2]));
        		sendReply(issuesForSource, message);
        		break;
            }
		}
		
		
	}

}
