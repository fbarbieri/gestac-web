package ort.proyecto.gestac.core.agents;

import com.mysql.jdbc.BestResponseTimeBalanceStrategy;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class SourceAgent extends GestacAgent {
	/**
	 * los que se agreguen por spring deben tener task=refresh
	 */

	public final String REFRESH = "refresh";
	public final String UPDATE = "update";
	
	private String task;
	
	@Override
	protected void setup() {	
//		if (task.equals(REFRESH)) {
//			addBehaviour(new SourceRefreshBehaviour());
//		} else if (task.equals(UPDATE)) {
//			//addBehaviour(new SourceUpdateBehaviour());
//		}
		addBehaviour(new QueryBehaviour());
	}

	class QueryBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
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
				case "updateSourceOnEvaluation":
					break;
				}
			} else {
				block();
			}
		}
		
	}
	
	
}
