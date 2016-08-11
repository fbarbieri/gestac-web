package ort.proyecto.gestac.core.agents.db;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.entities.Source;
import ort.proyecto.gestac.core.entities.repository.SourceRepository;

public class SourceDBAgent extends GestacAgent {

	private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(SourceDBAgent.class);

	@Autowired
	private SourceRepository sourceRepository;
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new SourceDBAgentBehaviour());
	}
	
	class SourceDBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				String content = message.getContent();
	            String[] parameters = content.split("&");
	            String operation = parameters[0];
	            switch (operation){
	            case DBAgentOperations.FIND_ALL_SOURCES:
	        		List<Source> allSources = sourceRepository.findAllByOrderByNameAsc();
	            	sendReply(allSources, message);
	        		break;
	            }
			} else {
				block();
			}
		}
		
		
	}

}
