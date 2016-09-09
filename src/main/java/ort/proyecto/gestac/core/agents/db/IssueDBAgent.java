package ort.proyecto.gestac.core.agents.db;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.IssueBestKnowledge;
import ort.proyecto.gestac.core.entities.repository.IssueBestKnowledgeRepository;
import ort.proyecto.gestac.core.entities.repository.IssueRepository;
import ort.proyecto.gestac.core.entities.repository.IssueSearchDataSource;

public class IssueDBAgent extends GestacAgent {
	
	private Logger logger = LoggerFactory.getLogger(IssueDBAgent.class);
	
	@Autowired
	private IssueSearchDataSource issueSearch;	
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private IssueBestKnowledgeRepository issueBestKnowledgeRepository;
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new IssueDBAgentBehaviour());
	}
	
	class IssueDBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = blockingReceive();
			try {
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
	        	case DBAgentOperations.GET_ISSUES_WITH_KNOWLEDGE_BY_OTHER_SOURCE:
	        		List<Issue> issuesWithKnowledge = issueSearch.getIssuesWithKnowledgeByOther(Long.parseLong(parameters[1]), 
	        				Long.parseLong(parameters[2]));
	        		sendReply(issuesWithKnowledge, message);
	        		break;
	        	case DBAgentOperations.ADD_ISSUE:
					try {
						Issue toAdd = getJsonMapper().readValue(parameters[1], Issue.class);
						Issue saved = issueRepository.save(toAdd);
						IssueBestKnowledge best = new IssueBestKnowledge(toAdd.getId(), toAdd, null, new Timestamp(System.currentTimeMillis()));
						issueBestKnowledgeRepository.save(best);
						sendReply(saved, message);
					} catch (IOException e) {
						logger.error("Error parsing issue, check the input for message addIssue: " + parameters[1], e);
						sendEmptyReply(message);
					}
					break;
	            }
			} catch (Exception e) {
				String operation = message.getContent()!=null?message.getContent():"";
				logger.error("Error for opertaion " + operation, e);
				sendEmptyReply(message);
			}
		}
		
		
	}

}
