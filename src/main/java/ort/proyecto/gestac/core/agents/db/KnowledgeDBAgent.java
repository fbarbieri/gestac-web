package ort.proyecto.gestac.core.agents.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.GestacAgent;
import ort.proyecto.gestac.core.agents.db.DBAgent.DBAgentBehaviour;
import ort.proyecto.gestac.core.entities.Knowledge;
import ort.proyecto.gestac.core.entities.repository.KnowledgeDataSource;

public class KnowledgeDBAgent extends GestacAgent {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private KnowledgeDataSource dataSource;
	
	private ObjectMapper jsonMapper = new ObjectMapper();

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new KnowledgeDBAgentBehaviour());
	}
	
	class KnowledgeDBAgentBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			//presentarse();
			
			ACLMessage message = blockingReceive();
            String content = message.getContent();
            String[] parameters = content.split("&");
            String operation = parameters[0];
            switch (operation){
            	case DBAgentOperations.GET_BEST_KNOWLEDGE_FOR_ISSUE:
            		Knowledge knowledge = dataSource.getBestKnowledgeForIssue(parameters[1]);
            		sendReply(knowledge, message);
            		break;
            	case DBAgentOperations.ADD_KNOWLEDGE_EVALUATION:
            		dataSource.addEvaluationToKnowledge(parameters[1], parameters[2],
            				parameters[3], parameters[4]);
            		break;
            }
		}
		
		private void sendReply(Knowledge data, ACLMessage messageToReplyTo) {
			ACLMessage reply = messageToReplyTo.createReply();
			try {
    			reply.setContent(jsonMapper.writeValueAsString(data));
    			send(reply);
    		} catch (Exception e) {
                e.printStackTrace();
            }
		}
	}
	
}
