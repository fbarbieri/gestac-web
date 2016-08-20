package ort.proyecto.gestac.core.agents.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.PruebaSpring;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;
import ort.proyecto.gestac.core.entities.repository.IssueSearchRepository;

public class DBAgent extends Agent {
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private IssueSearchRepository issueSearch;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@Override
	protected void setup() {
		super.setup();
		//Thread.currentThread().setContextClassLoader(classLoader);
		addBehaviour(new DBAgentBehaviour());
	}
	
	class DBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			//presentarse();
			
			ACLMessage message = blockingReceive();
            String content = message.getContent();
            String[] parameters = content.split("&");
            String operation = parameters[0];
            switch (operation){
            	case DBAgentOperations.FIND_ALL_AREAS:
//            		ACLMessage reply = message.createReply();
            		List<Area> areas = areaRepository.findAllByOrderByNameAsc();
//            		try {
//            			reply.setContent(jsonMapper.writeValueAsString(areas.toArray()));
//            			send(reply);
//            		} catch (Exception e) {
//                        e.printStackTrace();
//                    }
            		sendReply(areas, message);
            		break;
            }
			
			
		}
	
		private void sendReply(List data, ACLMessage messageToReplyTo) {
			ACLMessage reply = messageToReplyTo.createReply();
			try {
    			reply.setContent(jsonMapper.writeValueAsString(data.toArray()));
    			send(reply);
    		} catch (Exception e) {
                e.printStackTrace();
            }
		}
		
		private void presentarse() {
			try {
				System.out.println("Agente base de datos, " + this + ", " + this.myAgent.getName() + ", " + this.myAgent.getContainerController().getContainerName());
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

//	public void setClassLoader(ClassLoader classLoader) {
//		this.classLoader = classLoader;
//	}	
	
}
