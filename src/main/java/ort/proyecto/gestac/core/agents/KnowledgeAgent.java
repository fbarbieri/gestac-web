package ort.proyecto.gestac.core.agents;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class KnowledgeAgent extends GestacAgent {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeAgent.class);
	private Logger agentsLogger = LoggerFactory.getLogger("agents-activity");
	
//	public final String SEARCH_TO_UPDATE = "searchToUpdate";
//	public final String UPDATE = "update";
//	public final String QUERY = "query";

	private List<String> modes;
	
	//un comportamiento que reciba la consulta de mejor conocimiento.
	//aparte, el conocimiento que hace refresh
	//y el otro que hace update.

	public KnowledgeAgent() {
		super();
	}
	
//	public KnowledgeAgent(String... modes) {
//		super();
//		this.modes = Arrays.asList(modes);
//	}
	
	@Override
	protected void setup() {
//		if (modes!=null && modes.size()>0) {
//			if (modes.contains(SEARCH_TO_UPDATE)) {
//				addBehaviour(new SearchKnowledgesToUpdateBehaviour(this, tickerInterval));
//			}
//			if (modes.contains(QUERY)) {
//				addBehaviour(new QueryBehaviour());
//			}
//			if (mode.equals(UPDATE)) {
//				addBehaviour(b);
//			}
//		}
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
					case "getBestKnowledge":
						ACLMessage bestKnowledgeMessage = createMessage("KnowledgeDBAgent");
						bestKnowledgeMessage.setContent(DBAgentOperations.GET_BEST_KNOWLEDGE_FOR_ISSUE+"&"+parameters[1]);
						send(bestKnowledgeMessage);
						ACLMessage fromDB = blockingReceive(MessageTemplate.MatchConversationId(bestKnowledgeMessage.getConversationId()));
						sendReply(fromDB.getContent(), message);
						break;
					case "addKnowledgeEvaluation":
						ACLMessage addKnowledgeMessage = createMessage("KnowledgeDBAgent");
						addKnowledgeMessage.setContent(DBAgentOperations.ADD_KNOWLEDGE_EVALUATION+
								"&"+parameters[1]+"&"+parameters[2]+"&"+parameters[3]+"&"+parameters[4]);
						send(addKnowledgeMessage);
						break;
					case "addKnowledge":
						ACLMessage addKnowledge = createMessage("KnowledgeDBAgent");
						addKnowledge.setContent(DBAgentOperations.ADD_KNOWLEDGE + "&" + parameters[1]);
						send(addKnowledge);
						ACLMessage addKnowledgeReply = blockingReceive(MessageTemplate.MatchConversationId(addKnowledge.getConversationId()));
						sendReply(addKnowledgeReply.getContent(), message);
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
