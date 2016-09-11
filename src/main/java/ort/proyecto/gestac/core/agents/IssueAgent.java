package ort.proyecto.gestac.core.agents;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.repository.IssueSearchDataSource;

public class IssueAgent extends GestacAgent {

	private Logger logger = LoggerFactory.getLogger(IssueAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
	private String areaId;
	private String subjectId;
	private String incidentId;
	private String gravityId;
	
	@Autowired
	private IssueSearchDataSource issueSearch;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new myBehaviour());
	}
	
	/**
	 * según los datos que haya, llamar a dbagent
	 * @return
	 */
	class myBehaviour extends OneShotBehaviour {

		@Override
		public void action() {
			try {
				ACLMessage message = blockingReceive();
				agentsLogger.debug(this.myAgent.getName() + ", message recieved: " + message.getContent() + ", conversationId:" + message.getConversationId());
				List<Issue> result=null;
				if (areaId!=null && areaId.length()>0) {
					if (subjectId!=null && subjectId.length()>0 && 
							incidentId!=null && incidentId.length()>0 && 
							gravityId!=null && gravityId.length()>0) {
						result = messageToDB(DBAgentOperations.GET_ISSUES_SUBJECT_INCIDENT_GRAVITY+"&"+subjectId+"&"+incidentId+"&"+gravityId);
					} else if (subjectId!=null && subjectId.length()>0 && 
							incidentId!=null && incidentId.length()>0) {
						//result = issueSearch.getIssuesBySubjectIncident(Long.parseLong(subjectId), Long.parseLong(incidentId));
						result = messageToDB(DBAgentOperations.GET_ISSUES_SUBJECT_INCIDENT+"&"+subjectId+"&"+incidentId);
					} else if (subjectId!=null && subjectId.length()>0 && 
							gravityId!=null && gravityId.length()>0) {
						//result = issueSearch.getIssuesBySubjectGravity(Long.parseLong(subjectId), Long.parseLong(gravityId));
						result = messageToDB(DBAgentOperations.GET_ISSUES_SUBJECT_GRAVITY+"&"+subjectId+"&"+gravityId);
					}
				}
//				System.out.println("### - IssueAgent, contenedor: " + this.myAgent.getContainerController().getContainerName());
//				System.out.println("### - IssueAgent, mensaje de: " + message.getSender().getLocalName() + " conversación: " + message.getConversationId());
				ACLMessage reply = message.createReply();
				if (result!=null && result.size()>0) {
					reply.setContent(jsonMapper.writeValueAsString(result.toArray()));					
				}
				send(reply);
				agentsLogger.debug(this.myAgent.getName() + ", send reply: " + reply.getContent() + ", conversationId:" + reply.getConversationId());
			} catch (Exception e) {
				logger.error("Error on agent", e);
			} finally {
				agentsLogger.debug(this.myAgent.getName() + ", deleted itself from the container");
				myAgent.doDelete();
			}
		}
		
		private List<Issue> messageToDB(String content) {
			try {
				List<Issue> result = null;
				ACLMessage query = createMessage("IssueDBAgent");
				query.setContent(content);
				agentsLogger.debug(this.myAgent.getName() + ", send message to IssueDBAgent: " + content + ", conversationId:" + query.getConversationId());
				send(query);
				ACLMessage reply = blockingReceive();
				agentsLogger.debug(this.myAgent.getName() + ", reply from IssueDBAgent: " + reply.getContent() + ", conversationId:" + reply.getConversationId());
				Issue[] issues = jsonMapper.readValue(reply.getContent(), Issue[].class);
				if (issues!=null && issues.length>0) {
					result = Arrays.asList(issues);
				}				
				return result;
			} catch (IOException e) {
				logger.error("Error parsing Issues", e);
				return null;
			}
		}
	}
	
	
	public IssueAgent(String areaId, String subjectId, String incidentId, String gravityId, IssueSearchDataSource issueSearch) {
		super();
		this.areaId = areaId;
		this.subjectId = subjectId;
		this.incidentId = incidentId;
		this.gravityId = gravityId;
		this.issueSearch = issueSearch;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}
	public String getGravityId() {
		return gravityId;
	}
	public void setGravityId(String gravityId) {
		this.gravityId = gravityId;
	}	
	
	
}
