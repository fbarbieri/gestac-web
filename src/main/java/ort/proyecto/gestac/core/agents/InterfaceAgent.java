package ort.proyecto.gestac.core.agents;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Knowledge;

public class InterfaceAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger("ort.proyecto.gestac.core.agents.InterfaceAgent");
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	protected void setup() {
		super.setup();
		//Thread.currentThread().setContextClassLoader(classLoader);
	}
	
//	public Knowledge getBestKnowledge(String issueId) {
//		ACLMessage message = createMessage("KnowledgeAgent");
//	}
	
	@SuppressWarnings("unchecked")
	public List<Area> getAreas() {
		List<Area> list = null;
		try {
			ACLMessage message = createMessage("DBAgent");
			message.setContent(String.valueOf(DBAgentOperations.FIND_ALL_AREAS));
	        send(message);
	        ACLMessage reply = blockingReceive();
	        Area[] areas = jsonMapper.readValue(reply.getContent(), Area[].class);
			if (areas!=null && areas.length>0) {
				list = Arrays.asList(areas);
			}
		} catch (Exception e) {
			logger.error("Error getting Areas from agents, " + e.getMessage(), e);
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Issue> findIssues(String areaId, String subjectId, String incidentId, String gravityId) {
		List<Issue> list = null;
		try {
			ACLMessage message = createMessage("IssueSearchAgent");
			message.setContent("getIssues&"+areaId+"&"+subjectId+"&"+incidentId+"&"+gravityId);
	        send(message);
	        ACLMessage reply = blockingReceive();
	        Issue[] issues = jsonMapper.readValue(reply.getContent(), Issue[].class);
			if (issues!=null && issues.length>0) {
				list = Arrays.asList(issues);
			}
		} catch (Exception e) {
			logger.error("Error finding issues from agents, " + e.getMessage(), e);
			e.printStackTrace();
		}
		return list;
	}
	
	private ACLMessage createMessage(String agentName) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        AID idAgente = new AID(agentName, AID.ISLOCALNAME);
        message.addReceiver(idAgente);
        message.setConversationId(UUID.randomUUID().toString());
        return message;
    }
	
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		System.out.println(ev.getType());
		System.out.println(ev.getSource());
	}

//	public void setClassLoader(ClassLoader classLoader) {
//		this.classLoader = classLoader;
//	}

}
