package ort.proyecto.gestac.core.agents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class IssueManagementAgent extends GestacAgent {
	
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(IssueManagementAgent.class);
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
					case "getIssuesForSourceWithoutKnowledge":
						ACLMessage issuesForSourceMessage = createMessage("IssueDBAgent");
						issuesForSourceMessage.setContent(DBAgentOperations.GET_ISSUES_FOR_SOURCE_WITHOUT_KNOWLEDGE+
								"&"+parameters[1]+"&"+parameters[2]);
						send(issuesForSourceMessage);
						ACLMessage fromDB = blockingReceive(MessageTemplate.MatchConversationId(issuesForSourceMessage.getConversationId()));
						sendReply(fromDB.getContent(), message);
						break;
					case "getIssuesForSourceWithKnowledgeByOtherSource":
						ACLMessage issuesForSourceWithKnowledgeMessage = createMessage("IssueDBAgent");
						issuesForSourceWithKnowledgeMessage.setContent(DBAgentOperations.GET_ISSUES_WITH_KNOWLEDGE_BY_OTHER_SOURCE+
								"&"+parameters[1]+"&"+parameters[2]);
						send(issuesForSourceWithKnowledgeMessage);
						ACLMessage issuesForSourceWithKnowledgeReply = blockingReceive(MessageTemplate.MatchConversationId(
								issuesForSourceWithKnowledgeMessage.getConversationId()));
						sendReply(issuesForSourceWithKnowledgeReply.getContent(), message);
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
