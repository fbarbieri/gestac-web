package ort.proyecto.gestac.core.agents;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;

public class InterfaceAgent extends GuiAgent {

	private static final long serialVersionUID = 1L;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	protected void setup() {
		super.setup();
		//Thread.currentThread().setContextClassLoader(classLoader);
	}
	
	@SuppressWarnings("unchecked")
	public List<Area> getAreas() {
		List<Area> list = null;
		try {
			ACLMessage message = crearMensaje("DBAgent");
			message.setContent(String.valueOf(DBAgentOperations.FIND_ALL_AREAS));
	        send(message);
	        ACLMessage reply = blockingReceive();
			//List<Area> areas = (List<Area>) reply.getContentObject();
	        Area[] areas = jsonMapper.readValue(reply.getContent(), Area[].class);
			//Thread.currentThread().getContextClassLoader().loadClass("ort.proyecto.gestac.core.entities.Area");
			if (areas!=null && areas.length>0) {
				list = Arrays.asList(areas);
			}
//			for (Subject s : area.getSubjects()) {
//				System.out.println(s);
//			}
//	        System.out.println(areas[0].getSubjects().iterator().next().getName());
//	        System.out.println(areas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Issue> findIssues(String areaId, String subjectId, String incidentId, String gravityId) {
		List<Issue> list = null;
		try {
			ACLMessage message = crearMensaje("IssueSearchAgent");
			message.setContent("getIssues&"+areaId+"&"+subjectId+"&"+incidentId+"&"+gravityId);
	        send(message);
	        ACLMessage reply = blockingReceive();
	        Issue[] issues = jsonMapper.readValue(reply.getContent(), Issue[].class);
			if (issues!=null && issues.length>0) {
				list = Arrays.asList(issues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private ACLMessage crearMensaje(String agentName) {
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
