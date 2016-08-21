package ort.proyecto.gestac.core.agents.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.Issue;
import ort.proyecto.gestac.core.entities.PruebaSpring;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;
import ort.proyecto.gestac.core.entities.repository.IssueSearchDataSource;
import sun.util.logging.resources.logging;

public class DBAgent extends GestacAgent {
	
	private Logger logger = LoggerFactory.getLogger(DBAgent.class);
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private IssueSearchDataSource issueSearch;
	
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
            	case DBAgentOperations.ADD_AREA:
					try {
						areaRepository.save(jsonMapper.readValue(parameters[1], Area.class));
					} catch (IOException e) {
						logger.error("Error reading Area from json, area: " + parameters[1], e);
					}
            		break;
            	case DBAgentOperations.DELETE_AREA:
            		Area toDelete = areaRepository.findOne(Long.parseLong(parameters[1]));
            		if (toDelete.getIncidents().size()>0 || toDelete.getSources().size()>0 || 
            				toDelete.getSubjects().size()>0) {
            			sendReply(new Boolean(false), message);
            		} else {
            			areaRepository.delete(toDelete);
            			sendReply(new Boolean(true), message);
            		}
            		break;
            }
			
			
		}
	
//		private void sendReply(List data, ACLMessage messageToReplyTo) {
//			ACLMessage reply = messageToReplyTo.createReply();
//			try {
//    			reply.setContent(jsonMapper.writeValueAsString(data.toArray()));
//    			send(reply);
//    		} catch (Exception e) {
//                e.printStackTrace();
//            }
//		}
		
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
