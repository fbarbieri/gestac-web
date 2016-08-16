package ort.proyecto.gestac.core.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import ort.proyecto.gestac.core.agents.KnowledgeAgent.QueryBehaviour;
import ort.proyecto.gestac.core.agents.db.DBAgentOperations;

public class IssueManagementSearchAgent extends GestacAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
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
			} else {
				block();
			}
		}
	}
}
