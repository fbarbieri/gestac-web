package ort.proyecto.gestac.core.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFutureTask;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Gravity;
import ort.proyecto.gestac.core.entities.Incident;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.KnowledgeEvaluation;
import ort.proyecto.gestac.core.entities.Source;
import ort.proyecto.gestac.core.entities.Subject;

public class InterfaceAgent extends GuiAgent {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(InterfaceAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	protected void setup() {
		super.setup();
		try {
			agentsLogger.info(this.getName() + " started, container: " + this.getContainerController().getContainerName());
		} catch (ControllerException e) {
		}
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
				DBAgentOperations.FIND_SOURCE_BY_USER + "&" + source.getUserName());
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
			message = createMessage("SourceDBAgent", DBAgentOperations.SAVE_SOURCE + "&" + jsonMapper.writeValueAsString(source)
					+ "&" + source.getArea().getId());
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
	
	public List<Issue> getIssuesWithKnowledgeForSourceArea(String sourceId, String areaId) {
		List<Issue> result = null;
		ACLMessage message = createMessage("IssueManagementAgent", "getIssuesForSourceWithKnowledgeByOtherSource"+"&"+sourceId+"&"+areaId);
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		try {
			if (reply.getContent()!=null) {
				result = Arrays.asList(jsonMapper.readValue(reply.getContent(), Issue[].class));				
			}
		} catch (Exception e) {
			logger.error("Error getting issues for source with knowledge by other source, source: " + sourceId, e);
		}
		return result;
	}
	
	public Knowledge getBestKnowledge(String issueId) {
		Knowledge knowledge = null;
		ACLMessage message = createMessage("KnowledgeAgent", "getBestKnowledge"+"&"+issueId);
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		try {
			if (reply.getContent()!=null) {
				knowledge = jsonMapper.readValue(reply.getContent(), Knowledge.class);
			}
		} catch (IOException e) {
			logger.error("Error getting best knowledge for issue " + issueId, e);
			e.printStackTrace();
		}
		return knowledge;
	}
	
	private Area getAreaSourceIsBestFor(String sourceId) {
		Area area = null;
		ACLMessage message = createMessage("SourceAgent", "getAreaIfSourceIsBest"+"&"+sourceId);
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		try {
			if (reply.getContent()!=null) {
				area = jsonMapper.readValue(reply.getContent(), Area.class);				
			}
		} catch (Exception e) {
			logger.error("Error getting best source for area, source: " + sourceId, e);
		}
		return area;
	}
	
	public List<Issue> isBestSourceForHisArea(String sourceId) {
		
		Area area = getAreaSourceIsBestFor(sourceId);		
		//si tengo un área, es que esta fuente es la mejor para esa area.
		
		if (area!=null) {
			List<Issue> issuesToAnwser = getIssuesForSourceWithoutKnowledge(Long.parseLong(sourceId), area.getId());
			if (issuesToAnwser!=null) {
				return issuesToAnwser;
			} else {
				return new ArrayList<Issue>();
			}
		} else {
			return null; //no es mejor fuente
		}
	}
	
	public List<Issue> getIssuesForSourceWithoutKnowledge(long sourceId, long areaId) {
		List<Issue> result = null;
		ACLMessage message = createMessage("IssueManagementAgent", "getIssuesForSourceWithoutKnowledge"+"&"+sourceId+"&"+areaId);
		send(message);
		ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
		try {
			if (reply.getContent()!=null) {
				result = Arrays.asList(jsonMapper.readValue(reply.getContent(), Issue[].class));				
			}
		} catch (Exception e) {
			logger.error("Error getting best source for area, source: " + sourceId, e);
		}
		return result;
	}
	
	public Issue addIssue(Issue issue) {
		try {
			ACLMessage newIssueMessage = createMessage("IssueDBAgent", DBAgentOperations.ADD_ISSUE
					+ "&" + jsonMapper.writeValueAsString(issue));
			send(newIssueMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(newIssueMessage.getConversationId()));
			if (reply.getContent()!=null) {
				return jsonMapper.readValue(reply.getContent(), Issue.class);				
			} else {
				return null;
			}
		} catch (IOException e) {
			logger.error("Error converting from Knowledge to Json, id: " + issue.getId(), e);
			return null;
		} 
	}
	
	public Incident addIncident(Incident incident) {
		try {
			ACLMessage addIncidentMessage = createMessage("DBAgent", DBAgentOperations.ADD_INCIDENT + "&" + 
					jsonMapper.writeValueAsString(incident) + "&" + incident.getArea().getId());
			send(addIncidentMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(addIncidentMessage.getConversationId()));
			if (reply.getContent()!=null) {
				return jsonMapper.readValue(reply.getContent(), Incident.class);
			}
		} catch (IOException e) {
			logger.error("Error parsing Incident to/from json, id: " + incident.getId(), e);
		}
		return null;
	}

	public boolean deleteIncident(String id) {
		try {
			ACLMessage deleteIncidentMessage = createMessage("DBAgent", DBAgentOperations.DELETE_INCIDENT + "&" + id);
			send(deleteIncidentMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(deleteIncidentMessage.getConversationId()));
			return Boolean.parseBoolean(reply.getContent());
		} catch (Exception e) {
			logger.error("Error deleting incident " + id, e);
			return false;
		}
	}
	
	public Gravity addGravity(Gravity gravity) {
		try {
			ACLMessage addGravityMessage = createMessage("DBAgent", DBAgentOperations.ADD_GRAVITY + "&" + 
					jsonMapper.writeValueAsString(gravity) + "&" + gravity.getIncident().getId());
			send(addGravityMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(addGravityMessage.getConversationId()));
			if (reply.getContent()!=null) {
				return jsonMapper.readValue(reply.getContent(), Gravity.class);
			}
		} catch (IOException e) {
			logger.error("Error parsing Gravity to/from json, id: " + gravity.getId(), e);
		}
		return null;
	}

	public boolean deleteGravity(String id) {
		try {
			ACLMessage deleteGravityMessage = createMessage("DBAgent", DBAgentOperations.DELETE_GRAVITY + "&" + id);
			send(deleteGravityMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(deleteGravityMessage.getConversationId()));
			return Boolean.parseBoolean(reply.getContent());
		} catch (Exception e) {
			logger.error("Error deleting gravity " + id, e);
			return false;
		}
	}
	
	public Subject addSubject(Subject subject) {
		try {
			ACLMessage addSubjectMessage = createMessage("DBAgent", DBAgentOperations.ADD_SUBJECT + "&" + 
					jsonMapper.writeValueAsString(subject) + "&" + subject.getArea().getId());
			send(addSubjectMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(addSubjectMessage.getConversationId()));
			if (reply.getContent()!=null) {
				return jsonMapper.readValue(reply.getContent(), Subject.class);
			}
		} catch (IOException e) {
			logger.error("Error parsing Subject to/from json, id: " + subject.getId(), e);
		}
		return null;
	}
	
	public void addArea(Area area) {
		try {
			ACLMessage addAreaMessage = createMessage("DBAgent", DBAgentOperations.ADD_AREA + "&" + 
					jsonMapper.writeValueAsString(area));
			send(addAreaMessage);
		} catch (JsonProcessingException e) {
			logger.error("Error parsing Area to json, id: " + area.getId(), e);
		}		
	}
	
	public Long addKnowledge(Knowledge toSave) {
		Long newId = -1L;
		try {
			ACLMessage newKnowledge = createMessage("KnowledgeAgent", "addKnowledge"
					+ "&" + jsonMapper.writeValueAsString(toSave));
			send(newKnowledge);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(newKnowledge.getConversationId()));
			newId = Long.parseLong(reply.getContent());
		} catch (JsonProcessingException e) {
			logger.error("Error converting from Knowledge to Json, id: " + toSave.getId(), e);
		}	
		return newId;
	}
	
	public boolean deleteSubject(String id) {
		try {
			ACLMessage deleteSubjectMessage = createMessage("DBAgent", DBAgentOperations.DELETE_SUBJECT + "&" + id);
			send(deleteSubjectMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(deleteSubjectMessage.getConversationId()));
			return Boolean.parseBoolean(reply.getContent());
		} catch (Exception e) {
			logger.error("Error deleting subject " + id, e);
			return false;
		}
	}
	
	public boolean deleteArea(String id) {
		try {
			ACLMessage deleteAreaMessage = createMessage("DBAgent", DBAgentOperations.DELETE_AREA + "&" + id);
			send(deleteAreaMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(deleteAreaMessage.getConversationId()));
			return Boolean.parseBoolean(reply.getContent());
		} catch (Exception e) {
			logger.error("Error deleting area " + id, e);
			return false;
		}
	}
	
	public boolean deleteSource(String id) {
		try {
			ACLMessage checkMessage = createMessage("SourceDBAgent", DBAgentOperations.DELETE_SOURCE + "&" + id);
			send(checkMessage);
			ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(checkMessage.getConversationId()));
			return Boolean.parseBoolean(reply.getContent());
		} catch (Exception e) {
			logger.error("Error deleting area " + id, e);
			return false;
		}
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

	public List<Knowledge> getAllKnowledgesForIssue(String issueId) {
		List<Knowledge> list = null;
		try {
			ACLMessage message = createMessage("KnowledgeDBAgent", String.valueOf(DBAgentOperations.FIND_ALL_KNOWLEDGES_FOR_ISSUE + "&" + issueId));
	        send(message);
	        ACLMessage reply = blockingReceive(MessageTemplate.MatchConversationId(message.getConversationId()));
	        Knowledge[] array;
			array = jsonMapper.readValue(reply.getContent(), Knowledge[].class);
			if (array!=null && array.length>0) {
				list = Arrays.asList(array);
			}
		} catch (Exception e) {
			logger.error("Error getting knowledges for issue " + issueId, e);
			return null;
		}        
        return list;
	}

//	public void setClassLoader(ClassLoader classLoader) {
//		this.classLoader = classLoader;
//	}

}