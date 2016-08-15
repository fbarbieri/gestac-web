package ort.proyecto.gestac.core.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;
import ort.proyecto.gestac.core.entities.Source;

public class InterfaceAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger("ort.proyecto.gestac.core.agents.InterfaceAgent");
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	protected void setup() {
		super.setup();
		//Thread.currentThread().setContextClassLoader(classLoader);
	}
	
	public List<Area> getAreas() {
		List<Area> list = null;
		try {
			ACLMessage message = createMessage("DBAgent", String.valueOf(DBAgentOperations.FIND_ALL_AREAS));
	        send(message);
	        ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
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
	
	public Source loginSource(String userName, String password) {
		Source source = null;
		try {
			ACLMessage message = createMessage("SourceDBAgent", DBAgentOperations.GET_SOURCE_BY_LOGIN + "&" + userName + "&" + password);
			send(message);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
			if (reply.getContent()!=null) {
				source = jsonMapper.readValue(reply.getContent(), Source.class);				
			}
		} catch (IOException e) {
			logger.error("Error getting/parsing source by login, username: " + userName);
		}
		return source;
	}
	
	public List<Source> getSources() {
		List<Source> list = null;	
		try {
			ACLMessage message = createMessage("SourceDBAgent", DBAgentOperations.FIND_ALL_SOURCES);
			send(message);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
			Source[] sources = jsonMapper.readValue(reply.getContent(), Source[].class);
			if (sources!=null && sources.length>0) {
				list = Arrays.asList(sources);
			}
		} catch (IOException e) {
			logger.error("Error getting sources form agents, " + e.getMessage(),e);
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean sourceExists(Source source) {
		ACLMessage message = createMessage("SourceDBAgent", 
				DBAgentOperations.FIND_SOURCE_BY_NAME_MAIL + "&" + source.getName() + "&" + source.getMail());
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		if (reply.getContent()==null || reply.getContent().equals("")) {
			return false;
		} else {
			return true;			
		}
	}
	
	public boolean saveSource(Source source) {
		ACLMessage message;
		try {
			message = createMessage("SourceDBAgent", DBAgentOperations.SAVE_SOURCE + "&" + jsonMapper.writeValueAsString(source));
			send(message);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
			if (reply.getContent()!=null && reply.getContent().equals(DBAgentOperations.OK)) {
				return true;
			} else {
				return false;			
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Issue> findIssues(String areaId, String subjectId, String incidentId, String gravityId) {
		List<Issue> list = null;
		try {
			ACLMessage message = createMessage("IssueSearchAgent", "getIssues&"+areaId+"&"+subjectId+"&"+incidentId+"&"+gravityId);
	        send(message);
	        ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
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
	
	public Knowledge getBestKnowledge(String issueId) {
		Knowledge knowledge = null;
		ACLMessage message = createMessage("KnowledgeAgent", "getBestKnowledge"+"&"+issueId);
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		try {
			knowledge = jsonMapper.readValue(reply.getContent(), Knowledge.class);
		} catch (IOException e) {
			logger.error("Error getting best knowledge for issue " + issueId, e);
			e.printStackTrace();
		}
		return knowledge;
	}
	
	public List<Issue> isBestSourceForHisArea(String sourceId) {
		Area area = null;
		ACLMessage message = createMessage("SourceAgent", "getAreaIfSourceIsBest"+"&"+sourceId);
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		try {
			area = jsonMapper.readValue(reply.getContent(), Area.class);
		} catch (Exception e) {
			logger.error("Error getting best source for area, source: " + sourceId, e);
			e.printStackTrace();
		}
		
		//si tengo un área, es que esta fuente es la mejor para esa area.
		
		if (area!=null) {
			List<Issue> issuesToAnwser = interfaceAgent.getIssuesForSource(sourceId, area.getId());
			if (issuesToAnser!=null) {
				return issuesToAnwser;
			} else {
				return new ArrayList<Issue>();
			}
		} else {
			return null; //no es mejor fuente
		}
		
		
//		if (source!=null) {
//			List<Issue> issuesToAnwser = interfaceAgent.getIssuesForAgent();
//			
//			if (issuesToAnwser!=null) {
//				return issuesToAnwser;
//			} else {
//				new ArrayList();
//			}
//		} else {
//			return null;
//		}
		
		
		return null;
	}
	
	public void addKnowledgeEvaluation(KnowledgeEvaluation evaluation) {
		ACLMessage message = createMessage("KnowledgeAgent", "addKnowledgeEvaluation"
				+"&"+evaluation.getKnowledge().getId()
				+"&"+evaluation.getSimplicity()
				+"&"+evaluation.getUsedTime()
				+"&"+evaluation.getReuse());
		send(message);
	}
	
	private ACLMessage createMessage(String agentName, String content) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        AID idAgente = new AID(agentName, AID.ISLOCALNAME);
        message.addReceiver(idAgente);
        message.setConversationId(UUID.randomUUID().toString());
        message.setContent(content);
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
