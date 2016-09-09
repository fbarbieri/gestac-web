package ort.proyecto.gestac.core.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class SourceAgent extends GestacAgent {

	private Logger logger = LoggerFactory.getLogger(SourceAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
	@Override
	protected void setup() {	
		addBehaviour(new QueryBehaviour());
	}

	class QueryBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				try {
					String[] parameters = message.getContent().split("&");
					String operation = parameters[0];
					switch (operation){
					case "getAreaIfSourceIsBest":
						ACLMessage bestSourceMessage = createMessage("SourceDBAgent");
						bestSourceMessage.setContent(DBAgentOperations.GET_AREA_FOR_BEST_SOURCE+"&"+parameters[1]);
						send(bestSourceMessage);
						ACLMessage fromDB = blockingReceive(MessageTemplate.
								MatchConversationId(bestSourceMessage.getConversationId()));
						//source
						sendReply(fromDB.getContent(), message);
						break;
					}
				} catch (Exception e) {
					String operation = message.getContent()!=null?message.getContent():"";
					logger.error("Error for opertaion " + operation, e);
					sendEmptyReply(message);
				}
			} else {
				block();
			}
		}
		
	}
	
	
}
