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

	public KnowledgeAgent() {
		super();
	}
	
	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new QueryBehaviour());
	}
	
	class QueryBehaviour extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage message = receive();
			if (message!=null) {
				try {
					agentsLogger.debug(this.myAgent.getName() + ", message recieved: " + message.getContent() + ", conversationId:" + message.getConversationId());
					String[] parameters = message.getContent().split("&");
					String operation = parameters[0];
					switch (operation){
					case "getBestKnowledge":
						ACLMessage bestKnowledgeMessage = createMessage("KnowledgeDBAgent");
						bestKnowledgeMessage.setContent(DBAgentOperations.GET_BEST_KNOWLEDGE_FOR_ISSUE+"&"+parameters[1]);
						agentsLogger.debug(this.myAgent.getName() + ", message to KnowledgeDBAgent: " + bestKnowledgeMessage.getContent() + ", conversationId:" + bestKnowledgeMessage.getConversationId());
						send(bestKnowledgeMessage);
						ACLMessage fromDB = blockingReceive(MessageTemplate.MatchConversationId(bestKnowledgeMessage.getConversationId()));
						agentsLogger.debug(this.myAgent.getName() + ", reply from KnowledgeDBAgent: " + fromDB.getContent() + ", conversationId:" + fromDB.getConversationId());
						sendReply(fromDB.getContent(), message);
						break;
					case "addKnowledgeEvaluation":
						ACLMessage addKnowledgeEvaluationMessage = createMessage("KnowledgeDBAgent");
						addKnowledgeEvaluationMessage.setContent(DBAgentOperations.ADD_KNOWLEDGE_EVALUATION+
								"&"+parameters[1]+"&"+parameters[2]+"&"+parameters[3]+"&"+parameters[4]);
						agentsLogger.debug(this.myAgent.getName() + ", message to KnowledgeDBAgent: " + addKnowledgeEvaluationMessage.getContent() + ", conversationId:" + addKnowledgeEvaluationMessage.getConversationId());
						send(addKnowledgeEvaluationMessage);
						break;
					case "addKnowledge":
						ACLMessage addKnowledge = createMessage("KnowledgeDBAgent");
						addKnowledge.setContent(DBAgentOperations.ADD_KNOWLEDGE + "&" + parameters[1]);
						agentsLogger.debug(this.myAgent.getName() + ", message to KnowledgeDBAgent: " + addKnowledge.getContent() + ", conversationId:" + addKnowledge.getConversationId());
						send(addKnowledge);
						ACLMessage addKnowledgeReply = blockingReceive(MessageTemplate.MatchConversationId(addKnowledge.getConversationId()));
						agentsLogger.debug(this.myAgent.getName() + ", reply from KnowledgeDBAgent: " + addKnowledgeReply.getContent() + ", conversationId:" + addKnowledgeReply.getConversationId());
						sendReply(addKnowledgeReply.getContent(), message);
						break;
					}
				} catch (Exception e) {
					String operation = message.getContent()!=null?message.getContent():"";
					logger.error("Error for opertaion " + operation, e);
					sendEmptyReply(message);
				}
			} else {
				agentsLogger.debug(this.myAgent.getName() + ", called block(), waiting for messages");
				block();
			}
		}
		
	}

}
