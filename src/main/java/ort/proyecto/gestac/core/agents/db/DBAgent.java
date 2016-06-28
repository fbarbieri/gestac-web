package ort.proyecto.gestac.core.agents.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.entities.Area;
import ort.proyecto.gestac.core.entities.PruebaSpring;
import ort.proyecto.gestac.core.entities.repository.AreaRepository;

public class DBAgent extends Agent {
	
	@Autowired
	private PruebaSpring prueba;
	
	@Autowired
	private AreaRepository areaRepository;
	
	private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	//private ClassLoader classLoader;
	
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
            int operation = Integer.valueOf(parameters[0]);
            switch (operation){
            	case DBAgentOperations.FIND_ALL_AREAS:
            		ACLMessage reply = message.createReply();
            		List<Area> areas = areaRepository.findAll();
            		try {
            			System.out.println(areas.get(0).getSubjects().iterator().next().getName());
            			//reply.setContentObject(new ArrayList<>(areas));
            			reply.setContent(gson.toJson(areas.toArray()));
            			send(reply);
            		} catch (Exception e) {
                        e.printStackTrace();
                    }
            		break;
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
