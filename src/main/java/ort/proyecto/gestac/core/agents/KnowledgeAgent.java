package ort.proyecto.gestac.core.agents;

import java.util.Arrays;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class KnowledgeAgent extends GestacAgent {

	private static final long serialVersionUID = 1L;

	private List<String> modes;
	
	//un comportamiento que reciba la consulta de mejor conocimiento.
	//aparte, el conocimiento que hace refresh
	//y el otro que hace update.

	public KnowledgeAgent() {
		super();
	}
	
	public KnowledgeAgent(String... modes) {
		super();
		this.modes = Arrays.asList(modes);
	}
	
	@Override
	protected void setup() {
		if (modes!=null && modes.size()>0) {
			if (modes.contains("refresh")) {
//				addBehaviour(new RefreshKnowledgeScoreBehaviour());
			}
			if (modes.contains("query")) {
				addBehaviour(new SearchBehaviour());
			}
//			if (mode.equals("update")) {
//				addBehaviour(b);
//			}
		}
	}
	
	class SearchBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				String[] parameters = message.getContent().split("&");
				String operation = parameters[0];
				switch (operation){
				case "getBestKnowledge":
					ACLMessage toDB = createMessage("KnowledgeDBAgent");
					toDB.setContent(DBAgentOperations.GET_BEST_KNOWLEDGE_FOR_ISSUE+"&"+parameters[1]);
					send(toDB);
					ACLMessage fromDB = blockingReceive();
					sendReply(fromDB.getContent(), message);
				}
			} else {
				block();
			}
		}
		
	}

}
